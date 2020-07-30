package ru.training.karaf.model;

import java.util.Set;

public interface Unit extends Entity {
    //long getId();
    //String getName();
    String getNotation();
    Set<? extends Entity> getClimateParameters();
}
