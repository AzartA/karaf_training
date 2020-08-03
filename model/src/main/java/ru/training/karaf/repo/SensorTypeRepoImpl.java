package ru.training.karaf.repo;

import ru.training.karaf.model.SensorType;

import java.util.List;
import java.util.Optional;

public class SensorTypeRepoImpl implements SensorTypeRepo{
    @Override
    public Optional<? extends SensorType> addParams(long id, List<Long> paramsIds) {
        return Optional.empty();
    }

    @Override
    public List<? extends SensorType> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue) {
        return null;
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
