package ru.training.karaf.view;

import ru.training.karaf.model.SensorType;
import ru.training.karaf.repo.SensorTypeRepo;
import ru.training.karaf.repo.UserRepo;

import java.util.List;
import java.util.Optional;

public class SensorTypeViewImpl implements SensorTypeView {
    private SensorTypeRepo repo;
    private UserRepo auth;

    public SensorTypeViewImpl(SensorTypeRepo repo, UserRepo auth) {
        this.repo = repo;
        this.auth = auth;
    }

    @Override
    public Optional<? extends SensorType> addParams(long id, List<Long> paramsIds, String login) {
        return Optional.empty();
    }

    @Override
    public List<? extends SensorType> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz
    ) {
        return null;
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String login) {
        return 0;
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
