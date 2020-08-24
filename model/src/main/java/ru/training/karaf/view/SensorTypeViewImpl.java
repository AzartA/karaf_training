package ru.training.karaf.view;

import ru.training.karaf.model.SensorType;
import ru.training.karaf.model.SensorTypeDO;
import ru.training.karaf.repo.SensorTypeRepo;
import ru.training.karaf.repo.UserRepo;

import java.util.List;
import java.util.Optional;

public class SensorTypeViewImpl implements SensorTypeView {
    private SensorTypeRepo repo;
    private UserRepo auth;
    private Class<SensorTypeDO> type;

    public SensorTypeViewImpl(SensorTypeRepo repo, UserRepo auth) {
        this.repo = repo;
        this.auth = auth;
        type = SensorTypeDO.class;
    }

    @Override
    public Optional<? extends SensorType> addParams(long id, List<Long> paramsIds) {
        return Optional.empty();
    }

    @Override
    public List<? extends SensorType> getAll(
            List<FilterParam> filters, List<SortParam> sorts, int pg, int sz
    ) {
        return null;
    }

    @Override
    public long getCount(List<FilterParam> filters, int pg, int sz) {
        return 0;
    }

    @Override
    public Optional<? extends SensorType> create(SensorType entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends SensorType> update(long id, SensorType entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends SensorType> get(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends SensorType> delete(long id) {
        return Optional.empty();
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
