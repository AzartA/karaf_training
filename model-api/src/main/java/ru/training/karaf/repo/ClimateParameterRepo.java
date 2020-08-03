package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.Entity;

public interface ClimateParameterRepo {
    List<? extends ClimateParameter> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue);

    Optional<? extends ClimateParameter> create(ClimateParameter climateParameter);

    Optional<? extends ClimateParameter> update(long id, ClimateParameter climateParameter);

    Optional<? extends ClimateParameter> get(long id);

    Optional<? extends ClimateParameter> getByName(String name);

    Optional<? extends ClimateParameter> delete(long id);

    //Optional<? extends ClimateParameter> setUnits(long id, List<Long> unitIds);

    Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds);


}
