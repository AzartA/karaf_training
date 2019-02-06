package ru.training.karaf.service;

import java.util.List;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.Feedback;

public interface BookBuisnessLogicService {
    
    List<? extends Book> getAllBooks();
    Book getBook(String title);
    void createBook(Book book);
    void updateBook(String title, Book book);
    void deleteBook(String title);
    List<? extends Feedback> getBookFeedbacks(String title);
    
}
