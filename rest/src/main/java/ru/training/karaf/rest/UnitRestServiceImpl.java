package ru.training.karaf.rest;

import ru.training.karaf.model.Unit;
import ru.training.karaf.repo.UnitRepo;
import ru.training.karaf.rest.dto.UnitDTO;

import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class UnitRestServiceImpl implements UnitRestService {

    private UnitRepo repo;

    public void setRepo(UnitRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<UnitDTO> getAll() {
        return repo.getAll().stream().map(UnitDTO::new).collect(Collectors.toList());
    }

    @Override
    public UnitDTO create(UnitDTO unit) {
        return repo.create(unit).map(UnitDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public UnitDTO update(long id, UnitDTO unit) {
        Optional<? extends Unit> ent = repo.update(id, unit);
        return ent.map(UnitDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UnitDTO get(long id) {
        return repo.get(id).map(UnitDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        repo.delete(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
