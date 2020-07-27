package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.Location;

public interface LocationRepo {
    List<? extends Location> getAll();

    Optional<? extends Location> create(Location location);

    Optional<? extends Location> update(long id, Location location);

    Optional<? extends Location> get(long id);

    Optional<? extends Location> getByName(String name);

    Optional<? extends Location> delete(long id);

}
