package ru.training.karaf.service;

import java.util.List;
import java.util.Optional;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.Feedback;

public interface BookBuisnessLogicService {
    
    List<? extends Book> getAllBooks();
    
    Optional<? extends Book> getBook(String title);
    
    void createBook(Book book);
    
    void updateBook(String title, Book book);
    
    void deleteBook(String title);
    
    List<? extends Feedback> getBookFeedbacks(String title);
        
}
