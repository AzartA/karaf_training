package ru.training.karaf.repo;

import ru.training.karaf.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepo {
    List<? extends Location> getAll();

    Location create(Location location);

    Optional<? extends Location> update(String name, Location location);

    Optional<? extends Location> get(String name);

    String delete(String name);
}
