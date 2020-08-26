package ru.training.karaf.view;

import ru.training.karaf.exception.RestrictedException;
import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.ClimateParameterRepo;
import ru.training.karaf.wrapper.QueryParams;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ClimateParameterViewImpl implements ClimateParameterView {
    private final ClimateParameterRepo repo;
    private final Class<ClimateParameterDO> type;
    private final ViewUtil view;

    public ClimateParameterViewImpl(ClimateParameterRepo repo) {
        this.repo = repo;
        type = ClimateParameterDO.class;
        view = new ViewUtil();
    }

    @Override
    public Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds, User currentUser) {
        if (ChangingIsAllowed(currentUser)) {
            return repo.addUnits(id, unitIds);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public List<? extends ClimateParameter> getAll(
            List<FilterParam> filters, List<SortParam> sorts, int pg, int sz,
            User currentUser) {
        QueryParams query =  view.createQueryParams(filters, sorts, pg, sz);
        return repo.getAll(query);
    }

    @Override
    public long getCount(List<FilterParam> filters, int pg, int sz, User currentUser) {
        QueryParams query =  view.createQueryParams(filters, pg, sz);
        return repo.getCount(query);
    }

    @Override
    public Optional<? extends ClimateParameter> create(ClimateParameter entity, User currentUser) {
        if (ChangingIsAllowed(currentUser)) {
            return repo.create(entity);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends ClimateParameter> update(long id, ClimateParameter entity, User currentUser) {
        if (ChangingIsAllowed(currentUser)) {
            return repo.update(id, entity);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends ClimateParameter> get(long id, User currentUser) {
        return repo.get(id);
    }

    @Override
    public Optional<? extends ClimateParameter> delete(long id, User currentUser) {
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
