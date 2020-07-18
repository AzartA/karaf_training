package ru.training.karaf.repo;

import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.SensorDO;

import java.util.List;
import java.util.Optional;

public class SensorRepoImpl implements SensorRepo {
    @Override
    public void create(Sensor sensor) {
        SensorDO sensorForSave = new SensorDO();
        sensorForSave.setName(sensor.getName());

        //LocationDO location = new LocationDO(sensor.getLocation().getName());
        //sensorForSave.setLocation(location);
    }

    @Override
    public void update(String name, Sensor sensor) {

    }

    @Override
    public Optional<? extends Sensor> get(String name) {
        return Optional.empty();
    }

    @Override
    public void delete(String name) {

    }

    @Override
    public List<? extends Sensor> getAll() {
        return null;
    }
}
