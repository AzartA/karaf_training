package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.User;

public interface SensorView extends View<Sensor> {
    Optional<? extends Sensor> setSensorType(long id, long typeId, User currentUser);
    Optional<? extends Sensor> setLocation(long id, long locationId, User currentUser);
    Optional<? extends Sensor> addUsers(long id, List<Long> userIds, User currentUser);
    Optional<? extends Sensor> setXY(long id, long x, long y, User currentUser);

}
