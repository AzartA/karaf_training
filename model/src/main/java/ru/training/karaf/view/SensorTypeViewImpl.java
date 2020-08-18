package ru.training.karaf.view;

import ru.training.karaf.model.SensorType;
import ru.training.karaf.repo.SensorTypeRepo;
import ru.training.karaf.repo.UserAuthRepo;

import java.util.List;
import java.util.Optional;

public class SensorTypeViewImpl implements SensorTypeView {
    private SensorTypeRepo repo;
    private UserAuthRepo auth;

    public SensorTypeViewImpl(SensorTypeRepo repo, UserAuthRepo auth) {
        this.repo = repo;
        this.auth = auth;
    }

    @Override
    public Optional<? extends SensorType> addParams(long id, List<Long> paramsIds, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<List<? extends SensorType>> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String login
    ) {
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends SensorType> create(SensorType entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends SensorType> update(long id, SensorType entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends SensorType> get(long id, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends SensorType> delete(long id, String login) {
        return Optional.empty();
    }
}
