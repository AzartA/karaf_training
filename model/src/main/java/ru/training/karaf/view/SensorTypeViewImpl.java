package ru.training.karaf.view;

import ru.training.karaf.model.SensorType;
import ru.training.karaf.repo.SensorTypeRepo;

import java.util.List;
import java.util.Optional;

public class SensorTypeViewImpl implements SensorTypeRepo {

    @Override
    public Optional<? extends SensorType> addParams(long id, List<Long> paramsIds) {
        return Optional.empty();
    }

    @Override
    public List<? extends SensorType> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String[] auth
    ) {
        return null;
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String[] auth) {
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
}
