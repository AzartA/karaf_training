package ru.training.karaf.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.UserRepo;
import ru.training.karaf.rest.dto.UserDTO;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.AssertTrue;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public UserDTO create(UserDTO user) {
       /* if (repo.loginIsPresent(user.getLogin())) {
            throw new ValidationException("login must be unique");
        }*/
        return new UserDTO(repo.create(user));
    }

   /* @Override
    public UserDTO update(long id, UserDTO user) {
        Optional<? extends User> u = repo.update(id, user);
        if(u == null) return null;//throw new ValidationException("This login is already exist");
        return u.map(UserDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }*/
    @Override
    public UserDTO update(long id, UserDTO user) {
        return repo.updateById(id, user).map(UserDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }



    @Override
    public UserDTO get(long id) {
        return repo.get(id).map(UserDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UserDTO getByLogin(String login) {
        return repo.getByLogin(login).map(UserDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        repo.delete(id)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));

    }


}
