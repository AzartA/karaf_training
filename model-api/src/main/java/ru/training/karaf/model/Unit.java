package ru.training.karaf.model;

import java.util.Set;

public interface Unit extends Entity {
    String getNotation();

    Set<? extends Entity> getClimateParameters();
}
