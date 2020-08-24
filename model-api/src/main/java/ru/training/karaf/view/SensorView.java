package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.Sensor;

public interface SensorView extends View<Sensor> {
    Optional<? extends Sensor> setSensorType(long id, long typeId);
    Optional<? extends Sensor> setLocation(long id, long locationId);
    Optional<? extends Sensor> addUsers(long id, List<Long> userIds);
    Optional<? extends Sensor> setXY(long id, long x, long y);

}
