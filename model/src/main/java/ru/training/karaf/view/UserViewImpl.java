package ru.training.karaf.view;

import ru.training.karaf.model.User;
import ru.training.karaf.repo.UserRepo;

import java.util.List;
import java.util.Optional;

public class UserViewImpl implements UserRepo {

    @Override
    public Optional<? extends User> addSensors(long id, List<Long> sensorIds) {
        return Optional.empty();
    }

    @Override
    public boolean loginIsPresent(String login) {
        return false;
    }

    @Override
    public Optional<? extends User> getByLogin(String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends User> addRoles(long id, List<Long> rolesIds) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends User> removeRoles(long id, List<Long> rolesIds) {
        return Optional.empty();
    }

    @Override
    public List<? extends User> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String[] auth
    ) {
        return null;
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String[] auth) {
        return 0;
    }

    @Override
    public Optional<? extends User> create(User entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends User> update(long id, User entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends User> get(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends User> delete(long id) {
        return Optional.empty();
    }
}
