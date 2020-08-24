package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.Role;
import ru.training.karaf.model.RoleDO;
import ru.training.karaf.repo.RoleRepo;
import ru.training.karaf.repo.UserRepo;

public class RoleViewImpl implements RoleView {
    private RoleRepo repo;
    private UserRepo auth;
    private Class<RoleDO> type;

    public RoleViewImpl(RoleRepo repo, UserRepo auth) {
        this.repo = repo;
        this.auth = auth;
        type = RoleDO.class;
    }

    @Override
    public Optional<? extends Role> addUsers(long id, List<Long> userIds) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Role> removeUsers(long id, List<Long> userIds) {
        return Optional.empty();
    }

    @Override
    public List<? extends Role> getAll(
            List<FilterParam> filters, List<SortParam> sorts, int pg, int sz
    ) {
        return null;
    }

    @Override
    public long getCount(List<FilterParam> filters, int pg, int sz) {
        return 0;
    }

    @Override
    public Optional<? extends Role> create(Role entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Role> update(long id, Role entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Role> get(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Role> delete(long id) {
        return Optional.empty();
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
