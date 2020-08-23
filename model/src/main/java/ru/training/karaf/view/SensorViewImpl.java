package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.SensorDO;
import ru.training.karaf.model.User;
import ru.training.karaf.model.UserDO;
import ru.training.karaf.repo.SensorRepo;
import ru.training.karaf.repo.UserRepo;
import ru.training.karaf.wrapper.FilterParam;
import ru.training.karaf.wrapper.QueryParams;

public class SensorViewImpl implements SensorView {
    private SensorRepo repo;
    private UserRepo auth;
    private Class<SensorDO> type;
    private ViewImpl view;
    private User user;

    public SensorViewImpl(SensorRepo repo, UserRepo auth) {
        this.repo = repo;
        this.auth = auth;
        view = new ViewImpl();
        type = SensorDO.class;
    }

    @Override
    public Optional<? extends Sensor> setSensorType(long id, long typeId) {
        if (ChangingIsAllowed(id)) {
            return repo.setSensorType(id, typeId);
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> setLocation(long id, long locationId) {
        if (ChangingIsAllowed(id)) {
            return repo.setLocation(id, locationId);
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> addUsers(long id, List<Long> userIds) {
        if (ChangingIsAllowed(id)) {
            return repo.addUsers(id, userIds);
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> setXY(long id, long x, long y) {
        if (ChangingIsAllowed(id)) {
            return repo.setXY(id, x, y);
        }
        return Optional.empty();
    }

    @Override
    public List<? extends Sensor> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz
    ) {
        FilterParam authInfo = getAuthFilter();
        QueryParams query =  view.createQueryParams(by, order, field, cond, value, pg, sz, authInfo, type);
        return repo.getAll(query);
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        FilterParam authInfo = getAuthFilter();
        QueryParams query =  view.createQueryParams(field, cond, value, pg, sz, authInfo, type);
        return repo.getCount(query);
    }

    @Override
    public Optional<? extends Sensor> create(Sensor entity) {
        if(creatingIsAllowed()){
            return repo.create(entity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> update(long id, Sensor entity) {
        if (ChangingIsAllowed(id)) {
            return repo.update(id, entity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> get(long id) {
        if(allIsAllowed() || gettingIsAllowed(id)) {
            return repo.get(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends Sensor> delete(long id) {
        if (ChangingIsAllowed(id)) {
            return repo.delete(id);
        }
        return Optional.empty();
    }

    private boolean ChangingIsAllowed(long id) {
        user = SecurityUtils.getSubject().getPrincipals().oneByType(UserDO.class);
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin") || (roles.contains("Operator") &&
                user.getSensors().stream().mapToLong(Entity::getId).anyMatch(sId -> sId == id));
    }

    private FilterParam getAuthFilter(){
        user = SecurityUtils.getSubject().getPrincipals().oneByType(UserDO.class);
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        if (!roles.contains("Admin")) {
            return new FilterParam("users.id","=", Long.toString(user.getId()),type);
        }
        return null;
    }

    private boolean creatingIsAllowed() {
        user = SecurityUtils.getSubject().getPrincipals().oneByType(UserDO.class);
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin") || roles.contains("Operator");
    }
    private boolean gettingIsAllowed(long id) {
        user = SecurityUtils.getSubject().getPrincipals().oneByType(UserDO.class);
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return !roles.contains("Admin")&&
                user.getSensors().stream().mapToLong(Entity::getId).anyMatch(sId -> sId == id);
    }
    private boolean allIsAllowed() {
        user = SecurityUtils.getSubject().getPrincipals().oneByType(UserDO.class);
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin");
    }
}
