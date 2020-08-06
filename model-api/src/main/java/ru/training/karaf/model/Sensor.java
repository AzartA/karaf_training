package ru.training.karaf.model;

import java.util.Set;

public interface Sensor extends Entity {

    Location getLocation();

    //SensorType getType();
    Entity getType();

    Set<? extends Entity> getUsers();

   // List<? extends Measuring> getMeasurings();

}
