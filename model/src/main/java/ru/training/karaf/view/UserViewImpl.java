package ru.training.karaf.view;

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
    public Optional<? extends User> addSensors(long id, List<Long> sensorIds, String login) {
        return allIsAllowed(login)? repo.addSensors(id, sensorIds):Optional.empty();
    }

    @Override
    public boolean loginIsPresent(String login) {
       return repo.loginIsPresent(login);
    }

    @Override
    public Optional<? extends User> addRoles(long id, List<Long> rolesIds, String login) {
        return allIsAllowed(login)? repo.addSensors(id, rolesIds):Optional.empty();
    }

    @Override
    public Optional<? extends User> removeRoles(long id, List<Long> rolesIds, String login) {
        return allIsAllowed(login)? repo.addSensors(id, rolesIds):Optional.empty();
    }

    @Override
    public Optional<? extends User> getByLogin(String login) {
        return repo.getByLogin(login);
    }

    @Override
    public List<? extends User> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz
    ) {
        if(allIsAllowed(login) || gettingIsAllowed(login)) {
            QueryParams query = view.createQueryParams(by, order, field, cond, value, pg, sz, null, type);
            return repo.getAll(query);
        }
        return Collections.emptyList();
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String login) {
        if(allIsAllowed(login) || gettingIsAllowed(login)) {
            QueryParams query = view.createQueryParams(field, cond, value, pg, sz, null, type);
            return repo.getCount(query);
        }
        return 0;
    }

    @Override
    public Optional<? extends User> create(User entity, String login) {
        return allIsAllowed(login)? Optional.ofNullable(repo.create(entity)) :Optional.empty();
    }

    @Override
    public Optional<? extends User> update(long id, User entity, String login) {
        return allIsAllowed(login)? repo.update(id,entity) :Optional.empty();
    }

    @Override
    public Optional<? extends User> get(long id, String login) {
        if(allIsAllowed(login) || gettingIsAllowed(login)) {
            return repo.get(id);
        }
        return Optional.empty();// exception
    }

    @Override
    public Optional<? extends User> delete(long id, String login) {
        if(allIsAllowed(login)) {
            return repo.delete(id);
        }
        return Optional.empty();
    }

    private boolean ChangingIsAllowed(long id, String login) {
        User user = repo.getByLogin(login).get();
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin") || (roles.contains("Operator") &&
                user.getSensors().stream().mapToLong(Entity::getId).anyMatch(sId -> sId == id));
    }


    private boolean allIsAllowed(String login) {
        User user = repo.getByLogin(login).get();
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin");
    }

    private boolean gettingIsAllowed(String login) {
        User user = repo.getByLogin(login).get();
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin") || roles.contains("Operator");
    }
}
