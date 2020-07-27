package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.Unit;

public interface UnitRepo {
    List<? extends Unit> getAll();

    Optional<? extends Unit> create(Unit Unit);

    Optional<? extends Unit> update(long id, Unit Unit);

    Optional<? extends Unit> get(long id);

    Optional<? extends Unit> getByName(String name);

    Optional<? extends Unit> delete(long id);
}
