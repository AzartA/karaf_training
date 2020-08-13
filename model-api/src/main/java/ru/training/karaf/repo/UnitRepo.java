package ru.training.karaf.repo;

import ru.training.karaf.model.Unit;

import java.util.List;
import java.util.Optional;

public interface UnitRepo extends Repo<Unit> {
    Optional<? extends Unit > getByName(String name);
}
