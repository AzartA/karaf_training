package ru.training.karaf.rest;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.repo.UserRepo;
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
}
