package ru.training.karaf.service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.BookDO;
import ru.training.karaf.model.Feedback;
import ru.training.karaf.model.GenreDO;
import ru.training.karaf.model.UserDO;
import ru.training.karaf.repo.BookRepo;
import ru.training.karaf.repo.GenreRepo;
import ru.training.karaf.repo.UserRepo;

public class BookBuisnessLogicServiceImpl implements BookBuisnessLogicService {

    private BookRepo bookRepo;
    private GenreRepo genreRepo;
    private UserRepo userRepo;

    public void setBookRepo(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public void setGenreRepo(GenreRepo genreRepo) {
        this.genreRepo = genreRepo;
    }

    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }    
    
    public void init() {
        System.err.println("Book service loaded");
    }
    
    @Override
    public List<? extends Book> getAllBooks() {
        return bookRepo.getAllBooks();
    }

    @Override
    public Book getBook(String title) {
        try {
            return bookRepo.getBook(title).get();
        } catch (NoSuchElementException e) {
            System.err.println("Book not found");
            return null;
        }
    }

    @Override
    public void createBook(Book book) {
        if (bookRepo.getBook(book.getTitle()).isPresent()) {
            System.err.println("Book with specified title already exists");
            return;
        }
        try {
            GenreDO genre = (GenreDO)
                    genreRepo.getGenre(book.getGenre().getName()).get();
            BookDO bookToCreate = new BookDO();
            bookToCreate.setAuthor(book.getAuthor());
            bookToCreate.setGenre(genre);
            bookToCreate.setTitle(book.getTitle());
            bookToCreate.setYear(book.getYear());
            bookRepo.createBook(bookToCreate);
        } catch (NoSuchElementException e) {
            System.err.println("Genre not found");
        }
    }

    @Override
    public void updateBook(String title, Book book) {
        try {
            if (!title.equals(book.getTitle())) {
                System.err.println("title can not be changed");
                return;
            }
            GenreDO genre = (GenreDO)
                    genreRepo.getGenre(book.getGenre().getName()).get();
            
            BookDO bookToUpdate = (BookDO)bookRepo.getBook(title).get();
            bookToUpdate.setAuthor(book.getAuthor());
            bookToUpdate.setGenre(genre);
            bookToUpdate.setTitle(book.getTitle());
            bookToUpdate.setYear(book.getYear());
            bookRepo.updateBook(bookToUpdate);
        } catch (NoSuchElementException e) {
            System.err.println("Genre/book not found");
        }
    }

    @Override
    public void deleteBook(String title) {
        try {
            BookDO book = (BookDO)bookRepo.getBook(title).get();
            List<UserDO> users = (List<UserDO>)userRepo.getAllUsers();
            
            // TODO: check if comments are removed
            users.forEach(u -> {
                if (u.getBooks().remove(book)) {
                    userRepo.updateUser(u);
                }
            });
            
            bookRepo.deleteBook(title);
        } catch (NoSuchElementException e) {
            System.err.println("Book not found");
        }
    }

    @Override
    public List<? extends Feedback> getBookFeedbacks(String title) {
        try {
            BookDO book = (BookDO)bookRepo.getBook(title).get();
            return book.getFeedbacks();
        } catch (NoSuchElementException e) {
            System.err.println("Book not found");
        }
        return Collections.EMPTY_LIST;
    }
  
}
