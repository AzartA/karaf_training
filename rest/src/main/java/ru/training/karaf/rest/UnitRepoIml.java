package ru.training.karaf.rest;

import ru.training.karaf.model.Unit;
import ru.training.karaf.repo.Repo;

import java.util.List;
import java.util.Optional;

public class UnitRepoIml implements Repo<Unit> {

    @Override
    public List<? extends Unit> getAll() {
        return null;
    }

    @Override
    public Optional<? extends Unit> create(Unit type) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Unit> update(long id, Unit type) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Unit> get(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Unit> getByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends Unit> delete(long id) {
        return Optional.empty();
    }
}
