package ru.training.karaf.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.Feedback;
import ru.training.karaf.model.User;

public interface UserBuisnessLogicService {
    
    List<? extends User> getAllUsers();
    
    boolean createUser(User user);
    
    boolean updateUser(String libCard, User user);
    
    Optional<? extends User> getUser(String libCard);
    
    boolean deleteUser(String libCard);
    
    Set<? extends Book> getUserBooks(String libCard);
    
    List<? extends Feedback> getUserFeedbacks(String libCard);
    
    boolean addBook(String libCard, String title);
    
    boolean removeBook(String libCard, String title);
    
    boolean addFeedback(String libCard, Feedback feedback);
    
    boolean removeFeedback(String libCard, String title);
        
}
