package ru.training.karaf.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

import ru.training.karaf.model.Location;
import ru.training.karaf.model.User;

public interface LocationView extends View<Location>, ViewType {

    Optional<Object> getPlan(long id, OutputStream outputStream, User currentUser);

    long setPlan(long id, InputStream inputStream, String type, User currentUser);

    Optional<? extends Location> deletePlan(long id, User currentUser);

}
