package ru.training.karaf.model;



import ru.training.karaf.model.Unit;

import java.util.Set;

public interface ClimateParameter extends Entity {
    Set<? extends Entity> getUnits();
    Set<? extends Entity> getSensorTypes();
}
