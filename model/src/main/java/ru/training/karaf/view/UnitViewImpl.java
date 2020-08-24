package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.Unit;
import ru.training.karaf.model.UnitDO;
import ru.training.karaf.repo.UnitRepo;
import ru.training.karaf.repo.UserRepo;

public class UnitViewImpl implements UnitView {
    private UnitRepo repo;
    private UserRepo auth;
    private Class<UnitDO> type;

    public UnitViewImpl(UnitRepo repo, UserRepo auth) {
        this.repo = repo;
        this.auth = auth;
        type = UnitDO.class;
    }

    @Override
    public Optional<? extends Unit> getByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<? extends Unit> getAll(
            List<FilterParam> filters, List<SortParam> sorts, int pg, int sz
    ) {
        return null;
    }

    @Override
    public long getCount(List<FilterParam> filters, int pg, int sz) {
        return 0;
    }

    @Override
    public Optional<? extends Unit> create(Unit entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Unit> update(long id, Unit entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Unit> get(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Unit> delete(long id) {
        return Optional.empty();
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
