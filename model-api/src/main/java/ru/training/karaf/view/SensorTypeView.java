package ru.training.karaf.view;

import ru.training.karaf.model.SensorType;
import ru.training.karaf.model.User;

import java.util.List;
import java.util.Optional;

public interface SensorTypeView extends View<SensorType>, ViewType {
    Optional<? extends SensorType> addParams(long id, List<Long> paramsIds, User currentUser);
}
