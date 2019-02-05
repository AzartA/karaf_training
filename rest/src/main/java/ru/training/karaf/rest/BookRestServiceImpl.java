package ru.training.karaf.rest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.model.Book;
import ru.training.karaf.repo.BookRepo;
import ru.training.karaf.repo.GenreRepo;
import ru.training.karaf.rest.dto.BookDTO;
import ru.training.karaf.rest.dto.FeedbackDTO;

public class BookRestServiceImpl implements BookRestService {
    
    private BookRepo bookRepo;
    private GenreRepo genreRepo;
    
    public void setBookRepo(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public void setGenreRepo(GenreRepo genreRepo) {
        this.genreRepo = genreRepo;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepo
                .getAllBooks()
                .stream()
                .map(b -> new BookDTO(b))
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO getBook(String title) {
        return bookRepo.getBook(title)
                .map(b -> new BookDTO(b))
                .orElseThrow(() ->
                        new NotFoundException(Response.status(Response
                                .Status.NOT_FOUND)
                                .type(MediaType.TEXT_PLAIN)
                                .entity("Book not found")
                                .build()));
    }

    @Override
    public void createBook(BookDTO book) {
        if (bookRepo.getBook(book.getTitle()).isPresent()) {
            throw new WebApplicationException(Response
                    .status(Response.Status.CONFLICT)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Book with specified title already exists")
                    .build());
        }
        if (!genreRepo.getGenre(book.getGenre().getName()).isPresent()) {
            throw new NotFoundException(Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Genre not found")
                    .build());
        }
        bookRepo.createBook(book);
    }

    @Override
    public void updateBook(String title, BookDTO book) {
        if (!title.equals(book.getTitle())) {
            throw new WebApplicationException(Response
                    .status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Book's title cannot be changed")
                    .build());
        }
        if (!genreRepo.getGenre(book.getGenre().getName()).isPresent()) {
            throw new NotFoundException(Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Genre not found")
                    .build());
        }
        bookRepo.updateBook(title, book);
    }

    @Override
    public void deleteBook(String title) {
        bookRepo.deleteBook(title);
    }

    @Override
    public List<FeedbackDTO> getBookFeedbacks(String title) {
        try {
            Book book = bookRepo.getBook(title).get();
            return book.getFeedbacks()
                    .stream()
                    .map(f -> new FeedbackDTO(f))
                    .collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            throw new NotFoundException(Response.status(Response
                                .Status.NOT_FOUND)
                                .type(MediaType.TEXT_PLAIN)
                                .entity("Book not found")
                                .build());
        }
    }
}
