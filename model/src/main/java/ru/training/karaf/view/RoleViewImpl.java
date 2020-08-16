package ru.training.karaf.view;

import ru.training.karaf.model.Role;
import ru.training.karaf.repo.RoleRepo;

import java.util.List;
import java.util.Optional;

public class RoleViewImpl implements RoleRepo {

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
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String[] auth
    ) {
        return null;
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String[] auth) {
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
}
