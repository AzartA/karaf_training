package ru.training.karaf.service;

import java.util.List;
import java.util.Set;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.Feedback;
import ru.training.karaf.model.User;

public interface UserBuisnessLogicService {
    
    List<? extends User> getAllUsers();
    void createUser(User user);
    void updateUser(String libCard, User user);
    User getUser(String libCard);
    void deleteUser(String libCard);
    Set<? extends Book> getUserBooks(String libCard);
    List<? extends Feedback> getUserFeedbacks(String libCard);
    public void addBook(String libCard, String title);
    public void removeBook(String libCard, String title);
    public void addFeedback(String libCard, Feedback feedback);
    public void removeFeedback(String libCard, String title);
    
}
