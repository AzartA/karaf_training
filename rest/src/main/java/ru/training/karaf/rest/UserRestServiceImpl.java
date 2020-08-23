package ru.training.karaf.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import ru.training.karaf.model.User;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.UserDTO;
import ru.training.karaf.view.UserView;

public class UserRestServiceImpl implements UserRestService {
    private DefaultPasswordService passwordService;
    private UserView view;

    public void setView(UserView view) {
        this.view = view;
    }

    public void setPasswordService(DefaultPasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @Override
    public List<UserDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz,
                                String login) {
        return view.getAll(by, order, field, cond, value, pg, sz).stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz,
                              String login) {
        return new DTO<>(view.getCount(field, cond, value, pg, sz));
    }

    @Override
    public UserDTO create(UserDTO user,
                          String login) {

        /*if (view.loginIsPresent(user.getLogin())) {
            throw new ValidationException("login must be unique");
        }*/
        user.setPassword(passwordService.encryptPassword(user.getPassword()));
        return view.create(user).map(UserDTO::new).orElseThrow(() -> new ValidationException("login must be unique"));
    }

    @Override
    public UserDTO update(long id, UserDTO user,
                          String login) {
        user.setPassword(passwordService.encryptPassword(user.getPassword()));
        Optional<? extends User> u = view.update(id, user);
        return u.map(UserDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UserDTO get(long id,
                       String login) {
        return view.get(id).map(UserDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id,
                       String login) {
        view.delete(id)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UserDTO addSensors(long id, List<Long> sensorIds,
                              String login) {
        return view.addSensors(id, sensorIds, login).map(UserDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UserDTO addRoles(long id, List<Long> roleIds,
                            String login) {
        return view.addRoles(id, roleIds, login).map(UserDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UserDTO deleteRoles(long id, List<Long> roleIds,
                               String login) {
        return view.removeRoles(id, roleIds, login).map(UserDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
