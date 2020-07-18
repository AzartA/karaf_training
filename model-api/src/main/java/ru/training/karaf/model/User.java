package ru.training.karaf.model;

import java.util.Set;

public interface User {
    long getId();

    String getName();

    String getLogin();

    Set<String> getProperties();
}
