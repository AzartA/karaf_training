package ru.training.karaf.rest;


import ru.training.karaf.model.Role;
import ru.training.karaf.model.Sensor;
import ru.training.karaf.repo.RoleRepo;
import ru.training.karaf.rest.dto.RoleDTO;
import ru.training.karaf.rest.dto.SensorDTO;

import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleRestSeviceImpl implements  RoleRestService {
    private RoleRepo repo;

    public void setRepo(RoleRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<RoleDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        return repo.getAll(by, order, field, cond, value, pg, sz)
                .stream().map(RoleDTO::new).collect(Collectors.toList());
    }

    @Override
    public RoleDTO create(RoleDTO role) {
        return repo.create(role).map(RoleDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public RoleDTO update(long id, RoleDTO type) {
        Optional<? extends Role> l = repo.update(id, type);
        return l.map(RoleDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO addUsers(long id, List<Long> userIds) {
        return repo.addUsers(id, userIds).map(RoleDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO deleteUsers(long id, List<Long> userIds) {
        return repo.removeUsers(id, userIds).map(RoleDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO get(long id) {
        return repo.get(id).map(RoleDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        repo.delete(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
