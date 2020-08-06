package ru.training.karaf.model;

import java.util.Set;

public interface SensorType extends Entity {
    Capability getCapability();
    int getMinTime();
    Set<? extends Entity> getSensors();
    Set<? extends Entity> getParameters();
}
