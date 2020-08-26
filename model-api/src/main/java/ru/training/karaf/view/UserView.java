package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.User;

public interface UserView extends View<User>, ViewType {
    Optional<? extends ru.training.karaf.model.User> addSensors(long id, List<Long> sensorIds, User currentUser);
    Optional<? extends User> addRoles(long id, List<Long> rolesIds, User currentUser);
    Optional<? extends User> removeRoles(long id, List<Long> rolesIds, User currentUser);
    Optional<? extends User> getByLogin(String login);
}
