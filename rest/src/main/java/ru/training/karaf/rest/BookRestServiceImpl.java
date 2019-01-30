package ru.training.karaf.rest;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.repo.BookRepo;
import ru.training.karaf.rest.dto.BookDTO;
import ru.training.karaf.rest.dto.FeedbackDTO;

public class BookRestServiceImpl implements BookRestService {
    
    private BookRepo repo;
    
    public void setRepo(BookRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return repo.getAllBooks()
                .stream()
                .map(b -> new BookDTO(b))
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO getBook(String title) {
        return repo.getBook(title)
                .map(b -> new BookDTO(b))
                .orElseThrow(() -> new NotFoundException(Response.status
                    (Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_HTML).entity("Book not found").build()));
    }

    @Override
    public void createBook(BookDTO book) {
        repo.createBook(book);
    }

    @Override
    public void updateBook(String title, BookDTO book) {
        repo.updateBook(title, book);
    }

    @Override
    public void deleteBook(String title) {
        repo.deleteBook(title);
    }

    @Override
    public List<FeedbackDTO> getBookFeedbacks(String title) {
        return repo.getBookFeedbacks(title)
                .stream()
                .map(f -> new FeedbackDTO(f))
                .collect(Collectors.toList());
    }
}
