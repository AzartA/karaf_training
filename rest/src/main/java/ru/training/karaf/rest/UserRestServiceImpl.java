package ru.training.karaf.rest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.repo.UserRepo;
import ru.training.karaf.rest.dto.BookDTO;
import ru.training.karaf.rest.dto.FeedbackDTO;
import ru.training.karaf.rest.dto.UserDTO;

public class UserRestServiceImpl implements UserRestService {

    private UserRepo repo;
    
    public void setRepo(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<UserDTO> getAllUsers() {
       return repo
                .getAllUsers()
                .stream()
                .map(u -> new UserDTO(u))
                .collect(Collectors.toList());
    }

    @Override
    public void createUser(UserDTO user) {
        repo.createUser(user); 
    }

    @Override
    public void updateUser(String libCard, UserDTO user) {
        repo.updateUser(libCard, user);
    }

    @Override
    public UserDTO getUser(String libCard) {
        return repo.getUser(libCard)
                .map(u -> new UserDTO(u))
                .orElseThrow(() -> new NotFoundException(Response.status
                    (Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_HTML).entity("User not found").build()));
    }

    @Override
    public void deleteUser(String libCard) {
        repo.deleteUser(libCard);
    }

    @Override
    public Set<BookDTO> getUserBooks(String libCard) {
        return repo.getUserBooks(libCard)
                .stream()
                .map(b -> new BookDTO(b))
                .collect(Collectors.toSet());
    }

    @Override
    public void addBook(String libCard, BookDTO book) {
        repo.addBook(libCard, book);
    }

    @Override
    public void removeBook(String libCard, String title) {
        repo.removeBook(libCard, title);
    }
    
    @Override
    public List<FeedbackDTO> getUserFeedbacks(String libCard) {
        return repo.getUserFeedbacks(libCard)
                .stream()
                .map(f -> new FeedbackDTO(f))
                .collect(Collectors.toList());
    }
    
    @Override
    public void addFeedback(String libCard, FeedbackDTO feedback) {
        repo.addFeedback(libCard, feedback);
    }

    @Override
    public void removeFeedback(String libCard, String title) {
        repo.removeFeedback(libCard, title);
    }
}
