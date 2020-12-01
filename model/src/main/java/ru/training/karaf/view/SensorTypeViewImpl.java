package ru.training.karaf.view;

import ru.training.karaf.exception.RestrictedException;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.SensorType;
import ru.training.karaf.model.SensorTypeDO;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.SensorTypeRepo;
import ru.training.karaf.wrapper.QueryParams;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SensorTypeViewImpl implements SensorTypeView {
    private final SensorTypeRepo repo;
    private final Class<SensorTypeDO> type;
    private final ViewUtil view;

    public SensorTypeViewImpl(SensorTypeRepo repo) {
        this.repo = repo;
        type = SensorTypeDO.class;
        view = new ViewUtil();
    }

    @Override
    public Optional<? extends SensorType> addParams(long id, List<Long> paramsIds, User currentUser) {
        if (ChangingIsAllowed(currentUser)) {
            return repo.addParams(id, paramsIds);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public List<? extends SensorType> getAll(List<FilterParam> filters, List<SortParam> sorts, int pg, int sz, User currentUser) {
        QueryParams query = view.createQueryParams(filters, sorts, pg, sz);
        return repo.getAll(query);
    }

    @Override
    public long getCount(List<FilterParam> filters, int pg, int sz, User currentUser) {
        QueryParams query = view.createQueryParams(filters, pg, sz);
        return repo.getCount(query);
    }

    @Override
    public Optional<? extends SensorType> create(SensorType entity, User currentUser) {
        if (ChangingIsAllowed(currentUser)) {
            return repo.create(entity);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends SensorType> update(long id, SensorType entity, User currentUser) {
        if (ChangingIsAllowed(currentUser)) {
            return repo.update(id, entity);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends SensorType> get(long id, User currentUser) {
        return repo.get(id);
    }

    @Override
    public Optional<? extends SensorType> delete(long id, User currentUser) {
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
        return this;
    }

    private boolean ChangingIsAllowed(User user) {
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin") || roles.contains("Operator");
    }
}
