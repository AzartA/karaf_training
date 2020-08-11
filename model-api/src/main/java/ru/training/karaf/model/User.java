package ru.training.karaf.model;

import java.util.Set;

public interface User extends Entity {
    String getLogin();

    Set<String> getProperties();

    Set<? extends Entity> getSensors();

    Set<? extends Entity> getRoles();
}
