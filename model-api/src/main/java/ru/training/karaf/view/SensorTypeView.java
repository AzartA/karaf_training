package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.SensorType;
import ru.training.karaf.model.User;

public interface SensorTypeView extends View<SensorType>, ViewType {
    Optional<? extends SensorType> addParams(long id, List<Long> paramsIds, User currentUser);
}
