package ru.training.karaf.model;

import java.util.List;
import java.util.Set;

public interface Sensor {
    String getName();

    Location getLocation();

    SensorType getType();

    Set<? extends User> getUsers();

    List<? extends Measuring> getMeasurings();

}
