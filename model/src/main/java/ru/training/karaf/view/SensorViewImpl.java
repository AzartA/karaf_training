package ru.training.karaf.view;

import ru.training.karaf.exception.RestrictedException;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.SensorDO;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.SensorRepo;
import ru.training.karaf.wrapper.FilterParamImpl;
import ru.training.karaf.wrapper.QueryParams;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SensorViewImpl implements SensorView {
    private final SensorRepo repo;
    private final Class<SensorDO> type;
    private final ViewUtil view;

    public SensorViewImpl(SensorRepo repo) {
        this.repo = repo;
        view = new ViewUtil();
        type = SensorDO.class;
    }

    @Override
    public Optional<? extends Sensor> setSensorType(long id, long typeId, User currentUser) {
        if (changingIsAllowed(id, currentUser)) {
            return repo.setSensorType(id, typeId);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends Sensor> setLocation(long id, long locationId, User currentUser) {
        if (changingIsAllowed(id, currentUser)) {
            return repo.setLocation(id, locationId);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends Sensor> addUsers(long id, List<Long> userIds, User currentUser) {
        if (changingIsAllowed(id, currentUser)) {
            return repo.addUsers(id, userIds);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends Sensor> setXY(long id, long x, long y, User currentUser) {
        if (changingIsAllowed(id, currentUser)) {
            return repo.setXY(id, x, y);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public List<? extends Sensor> getAll(List<FilterParam> filters, List<SortParam> sorts, int pg, int sz, User currentUser) {
        getAuthFilter(currentUser).ifPresent(filters::add);
        QueryParams query = view.createQueryParams(filters, sorts, pg, sz);
        return repo.getAll(query);
    }

    @Override
    public long getCount(List<FilterParam> filters, int pg, int sz, User currentUser) {
        getAuthFilter(currentUser).ifPresent(filters::add);
        QueryParams query = view.createQueryParams(filters, pg, sz);
        return repo.getCount(query);
    }

    @Override
    public Optional<? extends Sensor> create(Sensor entity, User currentUser) {
        if (creatingIsAllowed(currentUser)) {
            return repo.create(entity);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends Sensor> update(long id, Sensor entity, User currentUser) {
        if (changingIsAllowed(id, currentUser)) {
            return repo.update(id, entity);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends Sensor> get(long id, User currentUser) {
        if (gettingIsAllowed(id, currentUser)) {
            return repo.get(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> delete(long id, User currentUser) throws RestrictedException {
        if (changingIsAllowed(id, currentUser)) {
            return repo.delete(id);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Class<? extends Entity> getType() {
        return type;
    }

    @Override
    public Class<? extends ViewType> getServiceClass() {
        return this.getClass();
    }

    @Override
    public ViewType get() {
        return this;
    }

    private boolean changingIsAllowed(long sensorId, User user) {
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin") || (roles.contains("Operator") &&
                user.getSensors().stream().mapToLong(Entity::getId).anyMatch(sId -> sId == sensorId));
    }

    private Optional<? extends FilterParam> getAuthFilter(User user) {
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        if (!roles.contains("Admin")) {
            return Optional.of(new FilterParamImpl("users.id", "=", Long.toString(user.getId()), type));
        }
        return Optional.empty();
    }

    private boolean creatingIsAllowed(User user) {
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin") || roles.contains("Operator");
    }

    private boolean gettingIsAllowed(long id, User user) {
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin") || user.getSensors().stream().mapToLong(Entity::getId).anyMatch(sId -> sId == id);
    }
}
