package ru.training.karaf.repo;

import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends Repo<User> {
    Optional<? extends User> addSensors(long id, List<Long> sensorIds);
   // Optional<? extends User> getByLogin(String login);
    boolean loginIsPresent(String login);
    Optional<? extends User> getByLogin(String login);
}
