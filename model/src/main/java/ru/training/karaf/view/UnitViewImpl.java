package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ru.training.karaf.exception.RestrictedException;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.Unit;
import ru.training.karaf.model.UnitDO;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.UnitRepo;
import ru.training.karaf.wrapper.QueryParams;

public class UnitViewImpl implements UnitView {
    private final UnitRepo repo;
    private final Class<UnitDO> type;
    private final ViewUtil view;

    public UnitViewImpl(UnitRepo repo) {
        this.repo = repo;
        type = UnitDO.class;
        view = new ViewUtil();
    }

    @Override
    public List<? extends Unit> getAll(
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
    public Optional<? extends Unit> create(Unit entity, User currentUser) {
        if (ChangingIsAllowed(currentUser)) {
            return repo.create(entity);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends Unit> update(long id, Unit entity, User currentUser) {
        if (ChangingIsAllowed(currentUser)) {
            return repo.update(id, entity);
        }
        throw new RestrictedException("Operation is restricted");
    }

    @Override
    public Optional<? extends Unit> get(long id, User currentUser) {
        return repo.get(id);
    }

    @Override
    public Optional<? extends Unit> delete(long id, User currentUser) {
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
