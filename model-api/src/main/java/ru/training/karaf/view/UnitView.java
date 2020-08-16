package ru.training.karaf.view;

import java.util.Optional;

import ru.training.karaf.model.Unit;

public interface UnitView extends View<Unit> {
    Optional<? extends Unit > getByName(String name, String login);
}
