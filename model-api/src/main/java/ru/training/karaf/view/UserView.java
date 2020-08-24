package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.User;

public interface UserView extends View<ru.training.karaf.model.User> {
    Optional<? extends ru.training.karaf.model.User> addSensors(long id, List<Long> sensorIds);
   boolean loginIsPresent(String login);
    Optional<? extends User> addRoles(long id, List<Long> rolesIds);
    Optional<? extends User> removeRoles(long id, List<Long> rolesIds);
    Optional<? extends User> getByLogin(String login);
}
