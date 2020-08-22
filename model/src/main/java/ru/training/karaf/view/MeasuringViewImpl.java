package ru.training.karaf.view;

import ru.training.karaf.model.Measuring;
import ru.training.karaf.repo.MeasuringRepo;
import ru.training.karaf.repo.UserRepo;

import java.util.List;
import java.util.Optional;

public class MeasuringViewImpl implements MeasuringView {
    private MeasuringRepo repo;
    private UserRepo auth;

    public MeasuringViewImpl(MeasuringRepo repo, UserRepo auth) {
        this.repo = repo;
        this.auth = auth;
    }

    @Override
    public List<? extends Measuring> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz
    ) {
        return null;
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz, String login) {
        return 0;
    }

    @Override
    public Optional<? extends Measuring> create(Measuring entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Measuring> update(long id, Measuring entity, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Measuring> get(long id, String login) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Measuring> delete(long id, String login) {
        return Optional.empty();
    }
}
