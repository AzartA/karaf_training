package ru.training.karaf.repo;

import ru.training.karaf.model.Location;

import java.util.List;
import java.util.Optional;

public interface Repo<T> {
    List<? extends T> getAll();

    Optional<? extends T> create(T type);

    Optional<? extends T> update(long id, T type);

    Optional<? extends T> get(long id);

    Optional<? extends T> getByName(String name);

    Optional<? extends T> delete(long id);
}
