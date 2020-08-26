package ru.training.karaf.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ru.training.karaf.exception.RestrictedException;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.Location;
import ru.training.karaf.model.LocationDO;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.LocationRepo;
import ru.training.karaf.wrapper.QueryParams;

public class LocationViewImpl implements LocationView {
    private final LocationRepo repo;
    private final Class<LocationDO> type;
    private final ViewUtil view;

    public LocationViewImpl(LocationRepo repo) {
        this.repo = repo;
        type = LocationDO.class;
        view = new ViewUtil();
    }

    @Override
    public Optional<Object> getPlan(long id, OutputStream outputStream, User currentUser) {
        if (ChangingIsAllowed(currentUser)) {
            return repo.getPlan(id, outputStream);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public long setPlan(long id, InputStream inputStream, String type, User currentUser) {
        if (ChangingIsAllowed(currentUser)) {
            return repo.setPlan(id, inputStream, type);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends Location> deletePlan(long id, User currentUser) {
        if (ChangingIsAllowed(currentUser)) {
            return repo.deletePlan(id);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public List<? extends Location> getAll(
            List<FilterParam> filters, List<SortParam> sorts, int pg, int sz,
            User currentUser
    ) {
        QueryParams query =  view.createQueryParams(filters, sorts, pg, sz);
        return repo.getAll(query);
    }

    @Override
    public long getCount(List<FilterParam> filters, int pg, int sz, User currentUser) {
        QueryParams query =  view.createQueryParams(filters, pg, sz);
        return repo.getCount(query);
    }

    @Override
    public Optional<? extends Location> create(Location entity, User currentUser) {
        if (ChangingIsAllowed(currentUser)) {
            return repo.create(entity);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends Location> update(long id, Location entity, User currentUser) {
        if (ChangingIsAllowed(currentUser)) {
            return repo.update(id, entity);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends Location> get(long id, User currentUser) {
        return repo.get(id);
    }

    @Override
    public Optional<? extends Location> delete(long id, User currentUser) {
        if (ChangingIsAllowed(currentUser)) {
            return repo.delete(id);
        }
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
        return  this;
    }

    private boolean ChangingIsAllowed(User user) {
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin") || roles.contains("Operator");
    }
}
