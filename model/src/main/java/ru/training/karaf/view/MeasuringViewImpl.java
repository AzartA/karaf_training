package ru.training.karaf.view;

import ru.training.karaf.model.Measuring;
import ru.training.karaf.repo.MeasuringRepo;

import java.util.List;
import java.util.Optional;

public class MeasuringViewImpl implements MeasuringRepo {

    @Override
    public List<? extends Measuring> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String[] auth
    ) {
        return null;
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String[] auth) {
        return 0;
    }

    @Override
    public Optional<? extends Measuring> create(Measuring entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Measuring> update(long id, Measuring entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Measuring> get(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Measuring> delete(long id) {
        return Optional.empty();
    }
}
