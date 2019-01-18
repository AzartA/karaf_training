package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.User;

public interface UserRepo {
    List<? extends User> getAll();
    void create(User user);
    void update(String login, User user);
    Optional<? extends User> get(String login);
    void delete(String login);
}
