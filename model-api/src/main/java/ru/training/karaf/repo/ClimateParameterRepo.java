package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.ClimateParameter;

public interface ClimateParameterRepo {
    List<? extends ClimateParameter> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue);

    Optional<? extends ClimateParameter> create(ClimateParameter ClimateParameter);

    Optional<? extends ClimateParameter> update(long id, ClimateParameter ClimateParameter);

    Optional<? extends ClimateParameter> get(long id);

    Optional<? extends ClimateParameter> getByName(String name);

    Optional<? extends ClimateParameter> delete(long id);

    Optional<? extends ClimateParameter> setUnits(long id, List<Long> unitIds);

    Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds);
}
