package ru.training.karaf.view;

import ru.training.karaf.exception.RestrictedException;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.Measuring;
import ru.training.karaf.model.MeasuringDO;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.MeasuringRepo;
import ru.training.karaf.wrapper.FilterParamImpl;
import ru.training.karaf.wrapper.QueryParams;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MeasuringViewImpl implements MeasuringView {
    private final MeasuringRepo repo;
    private final Class<MeasuringDO> type;
    private final ViewUtil view;

    public MeasuringViewImpl(MeasuringRepo repo) {
        this.repo = repo;
        type = MeasuringDO.class;
        view = new ViewUtil();
    }

    @Override
    public List<? extends Measuring> getAll(
            List<FilterParam> filters, List<SortParam> sorts, int pg, int sz,
            User currentUser
    ) {
        getAuthFilter(currentUser).ifPresent(filters::add);
        QueryParams query = view.createQueryParams(filters, sorts, pg, sz);
        return repo.getAll(query);
    }

    @Override
    public long getCount(List<FilterParam> filters, int pg, int sz, User currentUser) {
        getAuthFilter(currentUser).ifPresent(filters::add);
        QueryParams query = view.createQueryParams(filters, pg, sz);
        return repo.getCount(query);
    }

    @Override
    public Optional<? extends Measuring> create(Measuring entity, User currentUser) throws RestrictedException {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Measuring> update(long id, Measuring entity, User currentUser) throws RestrictedException {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Measuring> get(long id, User currentUser) throws RestrictedException {
        Optional<? extends Measuring> measuring = repo.get(id);
        if (measuring.isPresent() && gettingIsAllowed(measuring.get().getSensor().getId(), currentUser)) {
            return measuring;
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends Measuring> delete(long id, User currentUser) throws RestrictedException {
        Optional<? extends Measuring> measuring = repo.get(id);
        if (measuring.isPresent()) {
            if (ChangingIsAllowed(measuring.get().getSensor().getId(), currentUser)) {
                return repo.delete(id);
            }
            throw new RestrictedException("Operation is restricted");
        }
        return Optional.empty();
    }

    @Override
    public Class<? extends Entity> getType() {
        return type;
    }

    @Override
    public Class<? extends ViewType> getServiceClass() {
        return this.getClass();
    }

    @Override
    public ViewType get() {
        return this;
    }

    private Optional<? extends FilterParam> getAuthFilter(User user) {
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        if (!roles.contains("Admin")) {
            return Optional.of(new FilterParamImpl("sensor.users.id", "=", Long.toString(user.getId()), type));
        }
        return Optional.empty();
    }

    private boolean gettingIsAllowed(long sensorId, User user) {
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return !roles.contains("Admin") &&
                user.getSensors().stream().mapToLong(Entity::getId).anyMatch(sId -> sId == sensorId);
    }

    private boolean ChangingIsAllowed(long sensorId, User user) {
        Set<String> roles = user.getRoles().stream().map(Entity::getName).collect(Collectors.toSet());
        return roles.contains("Admin") || (roles.contains("Operator") &&
                user.getSensors().stream().mapToLong(Entity::getId).anyMatch(sId -> sId == sensorId));
    }
}
