package ru.training.karaf.view;

import ru.training.karaf.model.Location;
import ru.training.karaf.model.LocationDO;
import ru.training.karaf.repo.LocationRepo;
import ru.training.karaf.repo.UserRepo;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public class LocationViewImpl implements LocationView {
    private LocationRepo repo;
    private UserRepo auth;
    private Class<LocationDO> type;

    public LocationViewImpl(LocationRepo repo, UserRepo auth) {
        this.repo = repo;
        this.auth = auth;
        type = LocationDO.class;
    }

    @Override
    public Optional<? extends Location> getByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> getPlan(long id, OutputStream outputStream) {
        return Optional.empty();
    }

    @Override
    public long setPlan(long id, InputStream inputStream, String type) {
        return 0;
    }

    @Override
    public Optional<? extends Location> deletePlan(long id) {
        return Optional.empty();
    }

    @Override
    public List<? extends Location> getAll(
            List<FilterParam> filters, List<SortParam> sorts, int pg, int sz
    ) {
        return null;
    }

    @Override
    public long getCount(List<FilterParam> filters, int pg, int sz) {
        return 0;
    }

    @Override
    public Optional<? extends Location> create(Location entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Location> update(long id, Location entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Location> get(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Location> delete(long id) {
        return Optional.empty();
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
