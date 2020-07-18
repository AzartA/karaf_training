package ru.training.karaf.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ru.training.karaf.model.User;
import ru.training.karaf.repo.UserRepo;
import ru.training.karaf.rest.dto.UserDTO;

public class UserRestServiceImpl implements UserRestService {

    private UserRepo repo;
    
    public void setRepo(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<UserDTO> getAll() {
        return repo.getAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @Override
    public User create(UserDTO user) {
        if(!repo.loginIsUnique(user.getLogin())){
            throw new ValidationException("login must be unique");
        }
        return repo.create(user);
    }

    @Override
    public void update(String login, UserDTO user) {
        repo.update(login, user);
    }

    @Override
    public UserDTO get(String login) {
        return repo.get(login).map(UserDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_HTML).entity("User not found").build()));
    }

    @Override
    public void delete(String login) {
        repo.delete(login);
    }
}
