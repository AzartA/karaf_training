package ru.training.karaf.model;

import java.util.Set;

public interface ClimateParameter extends Entity {
    //long getId();
   // String getName();
    Set<? extends Entity> getUnits();

}
