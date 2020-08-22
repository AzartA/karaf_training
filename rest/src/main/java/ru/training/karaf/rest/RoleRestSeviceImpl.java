package ru.training.karaf.rest;


import ru.training.karaf.model.Role;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.RoleDTO;
import ru.training.karaf.view.RoleView;

import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleRestSeviceImpl implements  RoleRestService {
    private RoleView view;

    public void setView(RoleView view) {
        this.view = view;
    }

    @Override
    public List<RoleDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz,
                                String login) {
        return view.getAll(by, order, field, cond, value, pg, sz).stream().map(RoleDTO::new).collect(Collectors.toList());
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz,
                              String login) {
        return new DTO<>(view.getCount(field, cond, value, pg, sz, login));
    }

    @Override
    public RoleDTO create(RoleDTO role,
                          String login) {
        return view.create(role, login).map(RoleDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public RoleDTO update(long id, RoleDTO type,
                          String login) {
        Optional<? extends Role> l = view.update(id, type, login);
        return l.map(RoleDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO addUsers(long id, List<Long> userIds,
                            String login) {
        return view.addUsers(id, userIds, login).map(RoleDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO deleteUsers(long id, List<Long> userIds,
                               String login) {
        return view.removeUsers(id, userIds, login).map(RoleDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO get(long id,
                       String login) {
        return view.get(id, login).map(RoleDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id,
                       String login) {
        view.delete(id, login).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
