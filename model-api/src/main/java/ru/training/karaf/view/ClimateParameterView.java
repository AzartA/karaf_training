package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.User;

public interface ClimateParameterView extends View<ClimateParameter>, ViewType {

    Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds, User currentUser);

   // Optional<? extends ClimateParameter> addSensorTypes(long id, List<Long> typeIds);


}
