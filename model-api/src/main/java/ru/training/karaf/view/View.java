package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.Entity;

public interface View<T extends Entity> {
    List<? extends T> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz
    );

    long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz);

    Optional<? extends T> create(T entity);

    Optional<? extends T> update(long id, T entity);

    Optional<? extends T> get(long id);

    Optional<? extends T> delete(long id);
}
