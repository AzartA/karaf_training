package ru.training.karaf.repo;

import ru.training.karaf.model.Location;
import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.SensorType;
import ru.training.karaf.model.User;

import java.util.List;
import java.util.Optional;

public interface SensorRepo extends Repo<Sensor> {
    Optional<? extends Sensor> setSensorType(long id, long typeId);
    Optional<? extends Sensor> setLocation(long id, long locationId);
    Optional<? extends Sensor> addUsers(long id, List<Long> userIds);

}
