package ru.training.karaf.model;

import java.util.Set;

public interface User {
    String getName();
    String getLogin();
    Set<String> getProperties();
}
