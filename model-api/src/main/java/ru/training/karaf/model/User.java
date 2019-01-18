package ru.training.karaf.model;

import java.util.Set;

public interface User {
    String getFirstName();
    String getLastName();
    String getLogin();
    Integer getAge();
    String getAddress();
    Set<String> getProperties();
}
