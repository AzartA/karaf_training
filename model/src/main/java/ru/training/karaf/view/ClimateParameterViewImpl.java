package ru.training.karaf.view;

import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.repo.ClimateParameterRepo;
import ru.training.karaf.repo.UserRepo;

import java.util.List;
import java.util.Optional;

public class ClimateParameterViewImpl implements ClimateParameterView {
    private ClimateParameterRepo repo;
    private UserRepo auth;
    private Class<ClimateParameterDO> type;

    public ClimateParameterViewImpl(ClimateParameterRepo repo, UserRepo auth) {
        this.repo = repo;
        this.auth = auth;
        type = ClimateParameterDO.class;
    }

    @Override
    public Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends ClimateParameter> getByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<? extends ClimateParameter> getAll(
            List<FilterParam> filters, List<SortParam> sorts, int pg, int sz
    ) {
        return null;
    }

    @Override
    public long getCount(List<FilterParam> filters, int pg, int sz) {
        return 0;
    }

    @Override
    public Optional<? extends ClimateParameter> create(ClimateParameter entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends ClimateParameter> update(long id, ClimateParameter entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends ClimateParameter> get(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends ClimateParameter> delete(long id) {
        return Optional.empty();
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
