package ru.training.karaf.view;

import ru.training.karaf.model.Unit;
import ru.training.karaf.repo.UnitRepo;

import java.util.List;
import java.util.Optional;

public class UnitViewImpl implements UnitRepo {

    @Override
    public Optional<? extends Unit> getByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<? extends Unit> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String[] auth
    ) {
        return null;
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String[] auth) {
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
