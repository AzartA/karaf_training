package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ru.training.karaf.model.Entity;
import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.SensorRepo;
import ru.training.karaf.repo.UserAuthRepo;

public class SensorViewImpl implements SensorView {
    private SensorRepo repo;
    private UserAuthRepo auth;

    public SensorViewImpl(SensorRepo repo, UserAuthRepo auth) {
        this.repo = repo;
        this.auth = auth;
    }

    @Override
    public Optional<? extends Sensor> setSensorType(long id, long typeId, String login) {
        if (isAllowed(id, login)) {
            return repo.setSensorType(id, typeId);
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> setLocation(long id, long locationId, String login) {
        if (isAllowed(id, login)) {
            return repo.setLocation(id, locationId);
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> addUsers(long id, List<Long> userIds, String login) {
        if (isAllowed(id, login)) {
            return repo.addUsers(id, userIds);
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> setXY(long id, long x, long y, String login) {
        if (isAllowed(id, login)) {
            return repo.setXY(id, x, y);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<? extends Sensor>> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String login
    ) {
        String[] authInfo = new String[2];
        User user = auth.getUser(login);
        Set<Long> roles = user.getRoles().stream().map(Entity::getId).collect(Collectors.toSet());
        if (!roles.contains(1L)) {
            authInfo[0] = "users.id";
            authInfo[1] = Long.toString(user.getId());
        }
        return Optional.of(repo.getAll(by, order, field, cond, value, pg, sz, authInfo));
    }

    @Override
    public Optional<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String login) {
        String[] authInfo = new String[2];
        User user = auth.getUser(login);

        Set<Long> roles = user.getRoles().stream().map(Entity::getId).collect(Collectors.toSet());
        if (!roles.contains(1L)) {
            authInfo[0] = "users.id";
            authInfo[1] = Long.toString(user.getId());
        }
        return Optional.of(repo.getCount(field, cond, value, pg, sz, authInfo));
    }

    @Override
    public Optional<? extends Sensor> create(Sensor entity, String login) {
        User user = auth.getUser(login);

        Set<Long> roles = user.getRoles().stream().map(Entity::getId).collect(Collectors.toSet());
        if (roles.contains(1L) || roles.contains(3L)) {
            return repo.create(entity);
        }

        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> update(long id, Sensor entity, String login) {
        if (isAllowed(id, login)) {
            return repo.update(id, entity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> get(long id, String login) {
        User user = auth.getUser(login);
        Set<Long> roles = user.getRoles().stream().map(Entity::getId).collect(Collectors.toSet());
        Optional<? extends Sensor> sensor = repo.get(id);
        long userId = user.getId();
        if (sensor.isPresent() && !roles.contains(1L)) {
            if (sensor.get().getUsers().stream().mapToLong(Entity::getId).noneMatch(uId -> uId == userId)) {
                return Optional.empty();
            }
        }
        return sensor;
    }

    @Override
    public Optional<? extends Sensor> delete(long id, String login) {
        if (isAllowed(id, login)) {
            return repo.delete(id);
        }
        return Optional.empty();
    }

    private boolean isAllowed(long id, String login) {
        User user = auth.getUser(login);
        Set<Long> roles = user.getRoles().stream().map(Entity::getId).collect(Collectors.toSet());
        return roles.contains(1L) || (roles.contains(3L) &&
                user.getSensors().stream().mapToLong(Entity::getId).anyMatch(sId -> sId == id));
    }
}
