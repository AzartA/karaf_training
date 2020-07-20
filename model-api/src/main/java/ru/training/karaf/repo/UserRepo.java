package ru.training.karaf.repo;

import ru.training.karaf.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo {
    List<? extends User> getAll();

    User create(User user);

    Optional<? extends User> update(String login, User user);

    Optional<? extends User> get(String login);

    Optional<? extends User> delete(String login);

    boolean loginIsPresent(String login);
}
