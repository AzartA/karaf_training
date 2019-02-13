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
                        new NotFoundException(buildResponse(
                                Response.Status.NOT_FOUND, "Book not found")));
    }

    @Override
    public Response createBook(BookDTO book) {
        if (bookService.createBook(book)) {
            return buildResponse(Response.Status.CREATED,
                    "New book successfully created");
        } else {
            return buildResponse(Response.Status.INTERNAL_SERVER_ERROR,
                    "Cannot create new book");
        }
    }

    @Override
    public Response updateBook(String title, BookDTO book) {
        if (bookService.updateBook(title, book)) {
            return buildResponse(Response.Status.OK,
                    "Book successfully updated");
        } else {
            return buildResponse(Response.Status.INTERNAL_SERVER_ERROR,
                    "Cannot update book");
        }
    }

    @Override
    public Response deleteBook(String title) {
        if (bookService.deleteBook(title)) {
            return buildResponse(Response.Status.OK,
                    "Book successfully deleted");
        } else {
            return buildResponse(Response.Status.INTERNAL_SERVER_ERROR,
                    "Cannot delete book");
        }
    }

    @Override
    public List<FeedbackDTO> getBookFeedbacks(String title) {
        return bookService.getBookFeedbacks(title)
                .stream()
                .map(f -> new FeedbackDTO(f))
                .collect(Collectors.toList());
    }
    
    private Response buildResponse(Response.Status status, String desc) {
        return Response
                .status(status)
                .type(MediaType.TEXT_PLAIN)
                .entity(desc)
                .build();
    }
}
