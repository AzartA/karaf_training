package ru.training.karaf.model;

import java.util.Set;

public interface Role extends Entity {
    Set<? extends Entity> getUsers();
}
