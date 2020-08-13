package ru.training.karaf.repo;

import ru.training.karaf.model.Entity;
import ru.training.karaf.model.Sensor;

import java.util.List;
import java.util.Optional;

public interface Repo<T extends Entity> {
    List<? extends T> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue);
    List<? extends T> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz);
    long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz);
    Optional<? extends T> create(T entity);

    Optional<? extends T> update(long id, T entity);

    Optional<? extends T> get(long id);

    Optional<? extends T> delete(long id);


}
