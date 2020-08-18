package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.repo.ClimateParameterRepo;
import ru.training.karaf.repo.UserAuthRepo;

public class ClimateParameterViewImpl implements ClimateParameterView {
    private ClimateParameterRepo repo;
    private UserAuthRepo auth;

    public ClimateParameterViewImpl(ClimateParameterRepo repo, UserAuthRepo auth) {
        this.repo = repo;
        this.auth = auth;
    }

    @Override
    public Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends ClimateParameter> getByName(String name, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<List<? extends ClimateParameter>> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String login
    ) {
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends ClimateParameter> create(ClimateParameter entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends ClimateParameter> update(long id, ClimateParameter entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends ClimateParameter> get(long id, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends ClimateParameter> delete(long id, String login) {
        return Optional.empty();
    }
}
