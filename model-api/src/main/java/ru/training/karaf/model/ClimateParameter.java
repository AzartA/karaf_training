package ru.training.karaf.model;

import java.util.Set;

public interface ClimateParameter {
    long getId();
    String getName();
    Set<? extends Unit> getUnits();

}
