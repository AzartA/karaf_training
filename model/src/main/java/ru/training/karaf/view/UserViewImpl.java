package ru.training.karaf.view;

import ru.training.karaf.model.User;
import ru.training.karaf.repo.UserAuthRepo;
import ru.training.karaf.repo.UserRepo;

import java.util.List;
import java.util.Optional;

public class UserViewImpl implements UserView {
    private UserRepo repo;
    private UserAuthRepo auth;

    public UserViewImpl(UserRepo repo, UserAuthRepo auth) {
        this.repo = repo;
        this.auth = auth;
    }

    @Override
    public Optional<? extends User> addSensors(long id, List<Long> sensorIds, String login) {
        return Optional.empty();
    }

    @Override
    public boolean loginIsPresent(String login) {
        return false;
    }

    @Override
    public Optional<? extends User> addRoles(long id, List<Long> rolesIds, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends User> removeRoles(long id, List<Long> rolesIds, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<List<? extends User>> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String login
    ) {
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends User> create(User entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends User> update(long id, User entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends User> get(long id, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends User> delete(long id, String login) {
        return Optional.empty();
    }
}
