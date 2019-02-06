package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import ru.training.karaf.model.Book;

public interface BookRepo {
    List<? extends Book> getAllBooks();
    void createBook(Book book);
    void updateBook(Book book);
    Optional<? extends Book> getBook(String title);
    void deleteBook(String title);
}
