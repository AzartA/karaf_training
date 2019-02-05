package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import ru.training.karaf.model.User;

public interface UserRepo {
    List<? extends User> getAllUsers();
    void createUser(User user);
    void updateUser(String libCard, User user);
    Optional<? extends User> getUser(String libCard);
    void deleteUser(String libCard);
    
    void addBook(String libCard, String title);
}
