package ru.training.karaf.repo;

import ru.training.karaf.model.ClimateParameter;

import java.util.List;
import java.util.Optional;

public interface ClimateParameterRepo extends Repo<ClimateParameter> {

    Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds);

   // Optional<? extends ClimateParameter> addSensorTypes(long id, List<Long> typeIds);

    Optional<? extends ClimateParameter> getByName(String name);
}
