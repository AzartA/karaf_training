package ru.training.karaf.repo;

import ru.training.karaf.model.ClimateParameter;

import java.util.List;
import java.util.Optional;

public interface ClimateParameterRepo {
    List<? extends ClimateParameter> getAll();

    Optional<? extends ClimateParameter > create(ClimateParameter ClimateParameter);

    Optional<? extends ClimateParameter > update(long id, ClimateParameter ClimateParameter);

    Optional<? extends ClimateParameter > get(long id);

    Optional<? extends ClimateParameter > getByName(String name);

    Optional<? extends ClimateParameter > delete(long id);
}
