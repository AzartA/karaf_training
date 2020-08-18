package ru.training.karaf.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.Location;
import ru.training.karaf.repo.LocationRepo;
import ru.training.karaf.repo.UserAuthRepo;

public class LocationViewImpl implements LocationView {
    private LocationRepo repo;
    private UserAuthRepo auth;

    public LocationViewImpl(LocationRepo repo, UserAuthRepo auth) {
        this.repo = repo;
        this.auth = auth;
    }

    @Override
    public Optional<? extends Location> getByName(String name, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> getPlan(long id, OutputStream outputStream, String login) {
        return Optional.empty();
    }

    @Override
    public long setPlan(long id, InputStream inputStream, String type, String login) {
        return 0;
    }

    @Override
    public Optional<? extends Location> deletePlan(long id, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<List<? extends Location>> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String login
    ) {
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Location> create(Location entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Location> update(long id, Location entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Location> get(long id, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Location> delete(long id, String login) {
        return Optional.empty();
    }
}
