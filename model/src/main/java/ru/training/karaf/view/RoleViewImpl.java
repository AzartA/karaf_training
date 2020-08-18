package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.Role;
import ru.training.karaf.repo.RoleRepo;
import ru.training.karaf.repo.UserAuthRepo;

public class RoleViewImpl implements RoleView {
    private RoleRepo repo;
    private UserAuthRepo auth;

    public RoleViewImpl(RoleRepo repo, UserAuthRepo auth) {
        this.repo = repo;
        this.auth = auth;
    }

    @Override
    public Optional<? extends Role> addUsers(long id, List<Long> userIds, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Role> removeUsers(long id, List<Long> userIds, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<List<? extends Role>> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String login
    ) {
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Role> create(Role entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Role> update(long id, Role entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Role> get(long id, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Role> delete(long id, String login) {
        return Optional.empty();
    }
}
