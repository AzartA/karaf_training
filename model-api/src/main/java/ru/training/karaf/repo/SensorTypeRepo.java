package ru.training.karaf.repo;

import ru.training.karaf.model.SensorType;

import java.util.List;
import java.util.Optional;

public interface SensorTypeRepo extends Repo<SensorType>{
    Optional<? extends SensorType> addParams(long id, List<Long> paramsIds);
}
