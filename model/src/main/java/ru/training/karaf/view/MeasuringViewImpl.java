package ru.training.karaf.view;

import ru.training.karaf.model.Measuring;
import ru.training.karaf.model.MeasuringDO;
import ru.training.karaf.repo.MeasuringRepo;
import ru.training.karaf.repo.UserRepo;

import java.util.List;
import java.util.Optional;

public class MeasuringViewImpl implements MeasuringView {
    private MeasuringRepo repo;
    private UserRepo auth;
    private Class<MeasuringDO> type;

    public MeasuringViewImpl(MeasuringRepo repo, UserRepo auth) {
        this.repo = repo;
        this.auth = auth;
        type = MeasuringDO.class;
    }

    @Override
    public List<? extends Measuring> getAll(
            List<FilterParam> filters, List<SortParam> sorts, int pg, int sz
    ) {
        return null;
    }

    @Override
    public long getCount(List<FilterParam> filters, int pg, int sz) {
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

    @Override
    public Class<?> getType() {
        return type;
    }
}
