package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.Unit;
import ru.training.karaf.repo.UnitRepo;
import ru.training.karaf.repo.UserRepo;

public class UnitViewImpl implements UnitView {
    private UnitRepo repo;
    private UserRepo auth;

    public UnitViewImpl(UnitRepo repo, UserRepo auth) {
        this.repo = repo;
        this.auth = auth;
    }

    @Override
    public Optional<? extends Unit> getByName(String name, String login) {
        return Optional.empty();
    }

    @Override
    public List<? extends Unit> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz
    ) {
        return null;
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
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
}
