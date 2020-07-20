package ru.training.karaf.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.UserRepo;
import ru.training.karaf.rest.dto.UserDTO;

import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

public class UserRestServiceImpl implements UserRestService {

    private UserRepo repo;
    private ObjectMapper mapper = new ObjectMapper();

    public void setRepo(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<UserDTO> getAll() {
        return repo.getAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @Override
    public User create(UserDTO user) {
        if (!repo.loginIsPresent(user.getLogin())) {
            throw new ValidationException("login must be unique");
        }
        return repo.create(user);
    }

    @Override
    public UserDTO update(String login, UserDTO user) {
        //ToDo change name??
        if (!repo.loginIsPresent(user.getLogin())) {
            throw new ValidationException("login must be unique");
        }

        return repo.update(login, user).map(UserDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UserDTO get(String login) {
        return repo.get(login).map(UserDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(String login) {
        //ToDo repo??
        if(!repo.delete(login).isPresent()){
            new NotFoundException(Response.status(Response.Status.NOT_FOUND).build());
        }

    }
}
