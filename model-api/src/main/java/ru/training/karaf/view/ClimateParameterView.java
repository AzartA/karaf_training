package ru.training.karaf.view;

import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.User;

import java.util.List;
import java.util.Optional;

public interface ClimateParameterView extends View<ClimateParameter>, ViewType {
    Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds, User currentUser);
}
