package ru.training.karaf.repo;

import ru.training.karaf.model.Sensor;

import java.util.List;
import java.util.Optional;

public interface SensorRepo {
    void create(Sensor sensor);

    void update(String name, Sensor sensor);

    void delete(String name);

    Optional<? extends Sensor> get(String name);

    List<? extends Sensor> getAll();
}
