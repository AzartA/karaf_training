package ru.training.karaf.rest;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.rest.dto.BookDTO;
import ru.training.karaf.rest.dto.FeedbackDTO;
import ru.training.karaf.service.BookBuisnessLogicService;

public class BookRestServiceImpl implements BookRestService {
    
    private BookBuisnessLogicService bookService;

    public void setBookService(BookBuisnessLogicService bookService) {
        this.bookService = bookService;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks()
                .stream()
                .map(b -> new BookDTO(b))
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO getBook(String title) {
        return bookService.getBook(title)
                .map(b -> new BookDTO(b))
                .orElseThrow(() ->
                        new NotFoundException(
                                Response.status(Response.Status.NOT_FOUND)
                                        .type(MediaType.TEXT_PLAIN)
                                        .entity("Book not found")
                                        .build()));
    }

    @Override
    public void createBook(BookDTO book) {
        bookService.createBook(book);
    }

    @Override
    public void updateBook(String title, BookDTO book) {
        bookService.updateBook(title, book);
    }

    @Override
    public void deleteBook(String title) {
        bookService.deleteBook(title);
    }

    @Override
    public List<FeedbackDTO> getBookFeedbacks(String title) {
        return bookService.getBookFeedbacks(title)
                .stream()
                .map(f -> new FeedbackDTO(f))
                .collect(Collectors.toList());
    }
}
