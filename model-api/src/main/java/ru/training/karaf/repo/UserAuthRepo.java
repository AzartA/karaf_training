package ru.training.karaf.repo;

import ru.training.karaf.model.User;

import java.util.Optional;

public interface UserAuthRepo {
    Optional<? extends User> getUser(String login);
}
