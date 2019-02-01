package ru.training.karaf.rest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.BookRepo;
import ru.training.karaf.repo.UserRepo;
import ru.training.karaf.rest.dto.BookDTO;
import ru.training.karaf.rest.dto.FeedbackDTO;
import ru.training.karaf.rest.dto.UserDTO;

public class UserRestServiceImpl implements UserRestService {

    private UserRepo userRepo;
    private BookRepo bookRepo;
    
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    
    public void setBookRepo(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public List<UserDTO> getAllUsers() {
       return userRepo
                .getAllUsers()
                .stream()
                .map(u -> new UserDTO(u))
                .collect(Collectors.toList());
    }

    @Override
    public void createUser(UserDTO user) {
        if (userRepo.getUser(user.getLibCard()).isPresent()) {
            throw new WebApplicationException(Response
                    .status(Response.Status.CONFLICT)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Lib card is already taken")
                    .build());
        }
        userRepo.createUser(user); 
    }

    @Override
    public void updateUser(String libCard, UserDTO user) {
        if (userRepo.getUser(user.getLibCard()).isPresent()
                && !libCard.equals(user.getLibCard())) {
            throw new WebApplicationException(Response
                    .status(Response.Status.CONFLICT)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Lib card is already taken")
                    .build());
        }
        userRepo.updateUser(libCard, user);
    }

    @Override
    public UserDTO getUser(String libCard) {
        return userRepo.getUser(libCard)
                .map(u -> new UserDTO(u))
                .orElseThrow(() ->
                        new NotFoundException(Response
                                .status(Response.Status.NOT_FOUND)
                                .type(MediaType.TEXT_HTML)
                                .entity("User not found")
                                .build()));
    }

    @Override
    public void deleteUser(String libCard) {
        userRepo.deleteUser(libCard);
    }

    @Override
    public Set<BookDTO> getUserBooks(String libCard) {
//        return userRepo.getUserBooks(libCard)
//                .stream()
//                .map(b -> new BookDTO(b))
//                .collect(Collectors.toSet());
        try {
            User user = userRepo.getUser(libCard).get();
            System.err.println(user.getFeedbacks().size());
            System.err.println(user.getFeedbacks());
            return user.getBooks()
                    .stream()
                    .map(b -> new BookDTO(b))
                    .collect(Collectors.toSet());
        } catch (NoSuchElementException ex) {
            throw new WebApplicationException(Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("User not found")
                    .build());
        }
    }

    @Override
    public void addBook(String libCard, BookDTO book) {
        //repo.addBook(libCard, book);
        try {
            User user = userRepo.getUser(libCard).get();
            bookRepo.getBook(book.getTitle());
            UserDTO userToUpdate = new UserDTO(user);
            userToUpdate.getBooks().add(book);
            userRepo.updateUser(libCard, userToUpdate);
        } catch (NoSuchElementException ex) {
            throw new WebApplicationException(Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("User/book not found")
                    .build());
        }
    }

    @Override
    public void removeBook(String libCard, String title) {
        //userRepo.removeBook(libCard, title);
        try {
            User user = userRepo.getUser(libCard).get();
            BookDTO book = new BookDTO(bookRepo.getBook(title).get());
            UserDTO userToUpdate = new UserDTO(user);
            System.err.println("before: " + userToUpdate.getBooks());
            if (userToUpdate.getBooks().remove(book)) {
                System.err.println("Book removed");
            }
            System.err.println("After: " + userToUpdate.getBooks());
            userRepo.updateUser(libCard, userToUpdate);
        } catch (NoSuchElementException ex) {
            throw new WebApplicationException(Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("User/book not found")
                    .build());
        }
    }
    
    @Override
    public List<FeedbackDTO> getUserFeedbacks(String libCard) {
        try {
            User user = userRepo.getUser(libCard).get();
            return user.getFeedbacks()
                    .stream()
                    .map(f -> new FeedbackDTO(f))
                    .collect(Collectors.toList());
        } catch (NoSuchElementException ex) {
            throw new WebApplicationException(Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("User not found")
                    .build());
        }
//        return userRepo.getUserFeedbacks(libCard)
//                .stream()
//                .map(f -> new FeedbackDTO(f))
//                .collect(Collectors.toList());
    }
    
    @Override
    public void addFeedback(String libCard, FeedbackDTO feedback) {
        try {
            User user = userRepo.getUser(libCard).get();
            Book book = bookRepo.getBook(feedback.getBook().getTitle()).get();
            if (user.getBooks().contains(book)) {
                UserDTO userToUpdate = new UserDTO(user);
            }
        } catch (NoSuchElementException ex) {
            throw new WebApplicationException(Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("User/book not found")
                    .build());
        }
//userRepo.addFeedback(libCard, feedback);
    }

    @Override
    public void removeFeedback(String libCard, String title) {
        userRepo.removeFeedback(libCard, title);
    }
}
