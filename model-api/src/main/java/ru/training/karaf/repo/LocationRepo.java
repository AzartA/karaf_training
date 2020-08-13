package ru.training.karaf.repo;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.Location;

public interface LocationRepo extends Repo<Location> {
    Optional<? extends Location> getByName(String name);

    Optional<Object> getPlan(long id, OutputStream outputStream);

    long setPlan(long id, InputStream inputStream, String type);

    Optional<? extends Location> deletePlan(long id);

}
