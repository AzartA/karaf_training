package ru.training.karaf.view;

import ru.training.karaf.model.Entity;
import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.SensorRepo;

import ru.training.karaf.repo.UserAuthRepo;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SensorViewImpl implements SensorView{
    private SensorRepo repo;
    private UserAuthRepo auth;

    public SensorViewImpl(SensorRepo repo, UserAuthRepo auth) {
        this.repo = repo;
        this.auth = auth;
    }

    @Override
    public Optional<? extends Sensor> setSensorType(long id, long typeId, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> setLocation(long id, long locationId, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> addUsers(long id, List<Long> userIds, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> setXY(long id, long x, long y, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<List<? extends Sensor>> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String login
    ) {
        String[] authInfo = new String[2];
        Optional<? extends User> user = auth.getUser(login);
        if(!user.isPresent()){
            return Optional.empty();
        }
        Set<Long> roles = user.get().getRoles().stream().map(Entity::getId).collect(Collectors.toSet());
        if(!roles.contains(1L)){
            authInfo[0] = "users.id";
            authInfo[1] = Long.toString(user.get().getId());
        }
        return Optional.of(repo.getAll(by, order, field, cond, value, pg, sz, authInfo));
    }

    @Override
    public Optional<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String login) {
        String[] authInfo = new String[2];
        Optional<? extends User> user = auth.getUser(login);
        if(!user.isPresent()){
            return Optional.empty();
        }
        Set<Long> roles = user.get().getRoles().stream().map(Entity::getId).collect(Collectors.toSet());
        if(!roles.contains(1L)){
            authInfo[0] = "users.id";
            authInfo[1] = Long.toString(user.get().getId());
        }
        return Optional.of(repo.getCount(field, cond, value, pg, sz, authInfo));
    }

    @Override
    public Optional<? extends Sensor> create(Sensor entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> update(long id, Sensor entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> get(long id, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> delete(long id, String login) {
        return Optional.empty();
    }
}
