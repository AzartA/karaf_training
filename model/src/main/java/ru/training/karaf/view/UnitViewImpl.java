package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.Unit;
import ru.training.karaf.repo.UnitRepo;
import ru.training.karaf.repo.UserAuthRepo;

public class UnitViewImpl implements UnitView {
    private UnitRepo repo;
    private UserAuthRepo auth;

    public UnitViewImpl(UnitRepo repo, UserAuthRepo auth) {
        this.repo = repo;
        this.auth = auth;
    }

    @Override
    public Optional<? extends Unit> getByName(String name, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<List<? extends Unit>> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String login
    ) {
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Unit> create(Unit entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Unit> update(long id, Unit entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Unit> get(long id, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Unit> delete(long id, String login) {
        return Optional.empty();
    }
}
