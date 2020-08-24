package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.ClimateParameter;

public interface ClimateParameterView extends View<ClimateParameter> {

    Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds);

   // Optional<? extends ClimateParameter> addSensorTypes(long id, List<Long> typeIds);

    Optional<? extends ClimateParameter> getByName(String name);
}
