package ru.training.karaf.rest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.rest.dto.BookDTO;
import ru.training.karaf.rest.dto.FeedbackDTO;
import ru.training.karaf.rest.dto.UserDTO;
import ru.training.karaf.service.UserBuisnessLogicService;

public class UserRestServiceImpl implements UserRestService {

    private UserBuisnessLogicService userService;

    public void setUserService(UserBuisnessLogicService userService) {
        this.userService = userService;
    }

    @Override
    public List<UserDTO> getAllUsers() {
       return userService
                .getAllUsers()
                .stream()
                .map(u -> new UserDTO(u))
                .collect(Collectors.toList());
    }

    @Override
    public Response createUser(UserDTO user) {
        if (userService.createUser(user)) {
            return buildResponse(
                    Response.Status.CREATED,
                    "New user successfully created");
        } else {
            return buildResponse(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    "Cannot create user");
        }
    }

    
    @Override
    public Response updateUser(String libCard, UserDTO updatedUser) {
        if (userService.updateUser(libCard, updatedUser)) {
            return buildResponse(
                    Response.Status.OK,
                    "User successfully updated");
        } else {
            return buildResponse(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    "Cannot update user");
        }
    }

    @Override
    public UserDTO getUser(String libCard) {
        return userService.getUser(libCard)
                .map(u -> new UserDTO(u))
                .orElseThrow(() -> new NotFoundException(buildResponse(
                        Response.Status.NOT_FOUND,
                        "User not found")));
    }

    @Override
    public Response deleteUser(String libCard) {
        if (userService.deleteUser(libCard)) {
            return buildResponse(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    "User successfully deleted");
        } else {
            return buildResponse(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    "Cannot delete user");
        }
    }

    @Override
    public Set<BookDTO> getUserBooks(String libCard) {
        return userService.getUserBooks(libCard)
                .stream()
                .map(b -> new BookDTO(b))
                .collect(Collectors.toSet());
    }

    @Override
    public Response addBook(String libCard, String title) {
        if (userService.addBook(libCard, title)) {
            return buildResponse(
                    Response.Status.OK,
                    "Book added to the user list");
        } else {
            return buildResponse(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    "Cannot add a book to the list");
        }
    }

    @Override
    public Response removeBook(String libCard, String title) {
        if (userService.removeBook(libCard, title)) {
            return buildResponse(Response.Status.OK,
                    "Book deleted from the list");
        } else {
            return buildResponse(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    "Cannot delete a book from the list");
        }
    }
    
    @Override
    public List<FeedbackDTO> getUserFeedbacks(String libCard) {
        return userService.getUserFeedbacks(libCard)
                .stream()
                .map(f -> new FeedbackDTO(f))
                .collect(Collectors.toList());
    }
    
    @Override
    public Response addFeedback(String libCard, FeedbackDTO feedback) {
        if (userService.addFeedback(libCard, feedback)) {
            return buildResponse(
                    Response.Status.OK,
                    "Feedback added");
        } else {
            return buildResponse(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    "Cannot add new feedback");
        }
    }

    @Override
    public Response removeFeedback(String libCard, String title) {
        if (userService.removeFeedback(libCard, title)) {
            return buildResponse(
                    Response.Status.OK,
                    "Feedback deleted");
        } else {
            return buildResponse(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    "Cannot delete feedback");
        }
    }
    
    private Response buildResponse(Response.Status status, String desc) {
        return Response
                .status(status)
                .type(MediaType.TEXT_PLAIN)
                .entity(desc)
                .build();
    }
}
