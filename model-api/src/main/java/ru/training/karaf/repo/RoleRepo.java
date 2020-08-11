package ru.training.karaf.repo;

import ru.training.karaf.model.Role;
import ru.training.karaf.model.Sensor;

import java.util.List;
import java.util.Optional;

public interface RoleRepo extends Repo<Role>{
    Optional<? extends Role> addUsers(long id, List<Long> userIds);
    Optional<? extends Role> removeUsers(long id, List<Long> userIds);
}
