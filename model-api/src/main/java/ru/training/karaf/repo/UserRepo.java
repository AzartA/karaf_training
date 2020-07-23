package ru.training.karaf.repo;

import ru.training.karaf.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo {

    List<? extends User> getAll();

    User create(User user);

    Optional< ? extends  User> update(long id, User user);

    Optional<? extends User> updateById(long id, User user);

    Optional<? extends User> updateByLogin(String login, User user);

    Optional<? extends User> get(long id);

    Optional<? extends User> getByLogin(String login);

    Optional<? extends User> delete(long id);

    boolean loginIsPresent(String login);
}
