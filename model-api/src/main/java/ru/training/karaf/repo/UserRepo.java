package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.Feedback;
import ru.training.karaf.model.User;

public interface UserRepo {
    List<? extends User> getAllUsers();
    void createUser(User user);
    void updateUser(String libCard, User user);
    Optional<? extends User> getUser(String libCard);
    void deleteUser(String libCard);
    Set<? extends Book> getUserBooks(String libCard);
    void addBook(String libCard, Book requestedBook);
    void removeBook(String libCard, String title);
    List<? extends Feedback> getUserFeedbacks(String libCard);
    void addFeedback(String libCard, Feedback feedback);
    void removeFeedback(String libCard, String title);
}
