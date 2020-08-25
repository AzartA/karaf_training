package ru.training.karaf.view;

import ru.training.karaf.exception.RestrictedException;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.User;
import ru.training.karaf.model.UserDO;
import ru.training.karaf.repo.UserRepo;
import ru.training.karaf.wrapper.FilterParamImpl;
import ru.training.karaf.wrapper.QueryParams;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserViewImpl implements UserView {
    private final UserRepo repo;
    private final Class<UserDO> type;
    private final ViewUtil view;

    public UserViewImpl(UserRepo repo) {
        this.repo = repo;
        view = new ViewUtil();
        type = UserDO.class;
    }

    @Override
    public Optional<? extends User> addSensors(long id, List<Long> sensorIds, User currentUser) throws RestrictedException {

        if (changingIsAllowed(currentUser)) {
            return repo.addSensors(id, sensorIds);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends User> addRoles(long id, List<Long> rolesIds, User currentUser) {
        if (changingIsAllowed(currentUser)) {
            return repo.addSensors(id, rolesIds);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends User> removeRoles(long id, List<Long> rolesIds, User currentUser) {
        if (changingIsAllowed(currentUser)) {
            return repo.addSensors(id, rolesIds);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends User> getByLogin(String login) {
        return repo.getByLogin(login);
    }

    @Override
    public List<? extends User> getAll(
            List<FilterParam> filters, List<SortParam> sorts, int pg, int sz,
            User currentUser
    ) {
        setAuthFilter(currentUser).ifPresent(filters::add);
        QueryParams query = view.createQueryParams(filters, sorts, pg, sz);
        return repo.getAll(query);
    }

    @Override
    public long getCount(List<FilterParam> filters, int pg, int sz, User currentUser) {
        setAuthFilter(currentUser).ifPresent(filters::add);
        QueryParams query = view.createQueryParams(filters, pg, sz);
        return repo.getCount(query);
    }

    @Override
    public Optional<? extends User> create(User entity, User currentUser) {
        if (changingIsAllowed(currentUser)) {
            return Optional.ofNullable(repo.create(entity));
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends User> update(long id, User entity, User currentUser) {
        if (changingIsAllowed(currentUser)) {
            return repo.update(id, entity);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends User> get(long id, User currentUser) {
        if (gettingIsAllowed(id, currentUser)) {
            return repo.get(id);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends User> delete(long id, User currentUser) {
        if (changingIsAllowed(currentUser)) {
            return repo.delete(id);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Class<? extends Entity> getType() {
        return type;
    }

    private boolean changingIsAllowed(User user) {
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin");
    }

    private boolean gettingIsAllowed(long id, User user) {
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin") || roles.contains("Operator") || user.getId() == id;
    }

    private Optional<FilterParam> setAuthFilter(User user) {
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin") || roles.contains("Operator")? Optional.empty() :
                Optional.of(new FilterParamImpl("id", "=",Long.toString(user.getId()),type));
    }
}
