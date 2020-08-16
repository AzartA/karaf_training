package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.SensorType;

public interface SensorTypeView extends View<SensorType> {
    Optional<? extends SensorType> addParams(long id, List<Long> paramsIds, String login);
}
