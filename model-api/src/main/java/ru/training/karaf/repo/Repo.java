package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;

public interface Repo<T> {
    List<? extends T> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue);

    Optional<? extends T> create(T entity);

    Optional<? extends T> update(long id, T entity);

    Optional<? extends T> get(long id);

    Optional<? extends T> delete(long id);


}
