package ru.training.karaf.repo;

import ru.training.karaf.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepo {
    List<? extends Location> getAll();

    void create(Location location);

    void update(String name, Location location);

    Optional<? extends Location> get(String name);

    void delete(String name);
}
