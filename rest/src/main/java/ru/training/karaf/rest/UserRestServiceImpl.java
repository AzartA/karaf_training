package ru.training.karaf.rest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    public void createUser(UserDTO user) {
        userService.createUser(user);
    }

    
    @Override
    public void updateUser(String libCard, UserDTO updatedUser) {
        userService.updateUser(libCard, updatedUser);
    }

    @Override
    public UserDTO getUser(String libCard) {
        return new UserDTO(userService.getUser(libCard));
    }

    @Override
    public void deleteUser(String libCard) {
        userService.deleteUser(libCard);
    }

    @Override
    public Set<BookDTO> getUserBooks(String libCard) {
        return userService.getUserBooks(libCard)
                .stream()
                .map(b -> new BookDTO(b))
                .collect(Collectors.toSet());
    }

    @Override
    public void addBook(String libCard, String title) {
        userService.addBook(libCard, title);
    }

    @Override
    public void removeBook(String libCard, String title) {
        userService.removeBook(libCard, title);
    }
    
    @Override
    public List<FeedbackDTO> getUserFeedbacks(String libCard) {
        return userService.getUserFeedbacks(libCard)
                .stream()
                .map(f -> new FeedbackDTO(f))
                .collect(Collectors.toList());
    }
    
    @Override
    public void addFeedback(String libCard, FeedbackDTO feedback) {
        userService.addFeedback(libCard, feedback);
    }

    @Override
    public void removeFeedback(String libCard, String title) {
        userService.removeFeedback(libCard, title);
    }
}
