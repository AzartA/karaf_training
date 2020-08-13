package ru.training.karaf.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import ru.training.karaf.model.User;
import ru.training.karaf.repo.UserRepo;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.UserDTO;

public class UserRestServiceImpl implements UserRestService {

    private UserRepo repo;

    public void setRepo(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<UserDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        return repo.getAll(by, order, field, cond, value, pg, sz).stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        return new DTO<>(repo.getCount(field, cond, value, pg, sz));
    }

    @Override
    public UserDTO create(UserDTO user) {
        if (repo.loginIsPresent(user.getLogin())) {
            throw new ValidationException("login must be unique");
        }
        return repo.create(user).map(UserDTO::new).orElseThrow(() -> new ValidationException("login must be unique"));
    }

    @Override
    public UserDTO update(long id, UserDTO user) {
        Optional<? extends User> u = repo.update(id, user);
        return u.map(UserDTO::new)
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

    @Override
    public UserDTO addSensors(long id, List<Long> sensorIds) {
        return repo.addSensors(id, sensorIds).map(UserDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UserDTO addRoles(long id, List<Long> roleIds) {
        return repo.addRoles(id, roleIds).map(UserDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UserDTO deleteRoles(long id, List<Long> roleIds) {
        return repo.removeRoles(id, roleIds).map(UserDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
