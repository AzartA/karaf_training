package ru.training.karaf.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

import ru.training.karaf.model.Location;

public interface LocationView extends View<Location> {
    Optional<? extends Location> getByName(String name, String login);

    Optional<Object> getPlan(long id, OutputStream outputStream, String login);

    long setPlan(long id, InputStream inputStream, String type, String login);

    Optional<? extends Location> deletePlan(long id, String login);

}
