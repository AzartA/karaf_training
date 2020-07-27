package ru.training.karaf.repo;

import ru.training.karaf.model.Unit;

import java.util.List;
import java.util.Optional;

public interface UnitRepo {
    List<? extends Unit> getAll();

    Optional<? extends Unit > create(Unit unit);

    Optional<? extends Unit > update(long id, Unit unit);

    Optional<? extends Unit > get(long id);

    Optional<? extends Unit > getByName(String name);

    Optional<? extends Unit > delete(long id);
}
