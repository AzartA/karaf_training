package ru.training.karaf.view;

import org.apache.shiro.SecurityUtils;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.User;
import ru.training.karaf.model.UserDO;
import ru.training.karaf.repo.UserRepo;
import ru.training.karaf.wrapper.QueryParams;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserViewImpl implements UserView {
    private UserRepo repo;
    private Class<UserDO> type;
    private ViewImpl view;

    public UserViewImpl(UserRepo repo) {
        this.repo = repo;
        view = new ViewImpl();
        type = UserDO.class;
    }

    @Override
    public Optional<? extends User> addSensors(long id, List<Long> sensorIds) {
        return allIsAllowed()? repo.addSensors(id, sensorIds):Optional.empty();
    }

    @Override
    public boolean loginIsPresent(String login) {
       return repo.loginIsPresent(login);
    }

    @Override
    public Optional<? extends User> addRoles(long id, List<Long> rolesIds) {
        return allIsAllowed()? repo.addSensors(id, rolesIds):Optional.empty();
    }

    @Override
    public Optional<? extends User> removeRoles(long id, List<Long> rolesIds) {
        return allIsAllowed()? repo.addSensors(id, rolesIds):Optional.empty();
    }

    @Override
    public Optional<? extends User> getByLogin(String login) {
        return repo.getByLogin(login);
    }

    @Override
    public List<? extends User> getAll(
            List<FilterParam> filters, List<SortParam> sorts, int pg, int sz
    ) {
        if(allIsAllowed() || gettingIsAllowed()) {
            QueryParams query = view.createQueryParams(filters, sorts, pg, sz);
            return repo.getAll(query);
        }
        return Collections.emptyList();
    }

    @Override
    public long getCount(List<FilterParam> filters, int pg, int sz) {
        if(allIsAllowed() || gettingIsAllowed()) {
            QueryParams query = view.createQueryParams(filters, pg, sz);
            return repo.getCount(query);
        }
        return 0;
    }

    @Override
    public Optional<? extends User> create(User entity) {
        return allIsAllowed()? Optional.ofNullable(repo.create(entity)) :Optional.empty();
    }

    @Override
    public Optional<? extends User> update(long id, User entity) {
        return allIsAllowed()? repo.update(id,entity) :Optional.empty();
    }

    @Override
    public Optional<? extends User> get(long id) {
        if(allIsAllowed() || gettingIsAllowed()) {
            return repo.get(id);
        }
        return Optional.empty();// exception
    }

    @Override
    public Optional<? extends User> delete(long id) {
        if(allIsAllowed()) {
            return repo.delete(id);
        }
        return Optional.empty();
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    private boolean changingIsAllowed(long id) {
        User user = SecurityUtils.getSubject().getPrincipals().oneByType(UserDO.class);
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin") || (roles.contains("Operator") &&
                user.getSensors().stream().mapToLong(Entity::getId).anyMatch(sId -> sId == id));
    }


    private boolean allIsAllowed() {
        User user = SecurityUtils.getSubject().getPrincipals().oneByType(UserDO.class);
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin");
    }

    private boolean gettingIsAllowed() {
        User user = SecurityUtils.getSubject().getPrincipals().oneByType(UserDO.class);
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin") || roles.contains("Operator");
    }
}
