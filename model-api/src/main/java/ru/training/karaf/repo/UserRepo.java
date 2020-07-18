package ru.training.karaf.repo;

import ru.training.karaf.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo {
    List<? extends User> getAll();

    User create(User user);

    void update(String login, User user);

    Optional<? extends User> get(String login);

    void delete(String login);
}
