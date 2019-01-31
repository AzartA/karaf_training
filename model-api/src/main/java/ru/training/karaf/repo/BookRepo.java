package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.Feedback;

public interface BookRepo {
    List<? extends Book> getAllBooks();
    void createBook(Book book);
    void updateBook(String title, Book book);
    Optional<? extends Book> getBook(String title);
    void deleteBook(String title);
    List<? extends Feedback> getBookFeedbacks(String title);
}
