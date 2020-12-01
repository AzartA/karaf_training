package ru.training.karaf.view;

import ru.training.karaf.exception.RestrictedException;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.Role;
import ru.training.karaf.model.RoleDO;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.RoleRepo;
import ru.training.karaf.wrapper.QueryParams;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleViewImpl implements RoleView {
    private final RoleRepo repo;
    private final Class<RoleDO> type;
    private final ViewUtil view;

    public RoleViewImpl(RoleRepo repo) {
        this.repo = repo;
        this.view = new ViewUtil();
        type = RoleDO.class;
    }

    @Override
    public Optional<? extends Role> addUsers(long id, List<Long> userIds, User currentUser) {
        if (allIsAllowed(currentUser)) {
            return repo.addUsers(id, userIds);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends Role> removeUsers(long id, List<Long> userIds, User currentUser) {
        if (allIsAllowed(currentUser)) {
            return repo.removeUsers(id, userIds);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public List<? extends Role> getAll(
            List<FilterParam> filters, List<SortParam> sorts, int pg, int sz,
            User currentUser
    ) {
        QueryParams query = view.createQueryParams(filters, sorts, pg, sz);
        return repo.getAll(query);
    }

    @Override
    public long getCount(List<FilterParam> filters, int pg, int sz, User currentUser) {
        QueryParams query = view.createQueryParams(filters, pg, sz);
        return repo.getCount(query);
    }

    @Override
    public Optional<? extends Role> create(Role entity, User currentUser) {
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends Role> update(long id, Role entity, User currentUser) {
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends Role> get(long id, User currentUser) {
        return repo.get(id);
    }

    @Override
    public Optional<? extends Role> delete(long id, User currentUser) {
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

    private boolean allIsAllowed(User user) {
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin");
    }
}
