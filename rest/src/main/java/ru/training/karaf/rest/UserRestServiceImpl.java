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
    public List<UserDTO> getAll() {
        List<UserDTO> result = repo.getAll().stream().map(u -> new UserDTO(u)).collect(Collectors.toList());
        return result;
    }

    @Override
    public void create(UserDTO user) {
        repo.create(user); 
    }

    @Override
    public void update(String login, UserDTO user) {
        repo.update(login, user);
    }

    @Override
    public UserDTO get(String login) {
        return repo.get(login).map(u -> new UserDTO(u))
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_HTML).entity("User not found").build()));
    }

    @Override
    public void delete(String login) {
        repo.delete(login);
    }
}
