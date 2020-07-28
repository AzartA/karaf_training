package ru.training.karaf.rest;


import ru.training.karaf.model.ClimateParameter;

import ru.training.karaf.repo.ClimateParameterRepo;
import ru.training.karaf.rest.dto.ClimateParameterDTO;
import ru.training.karaf.rest.dto.LocationDTO;

import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClimateParameterRestServiceImpl implements ClimateParameterRestService {
    private ClimateParameterRepo repo;

    public void setRepo(ClimateParameterRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<ClimateParameterDTO> getAll() {
        return repo.getAll().stream().map(ClimateParameterDTO::new).collect(Collectors.toList());
    }

    @Override
    public ClimateParameterDTO create(ClimateParameterDTO parameter) {
        return repo.create(parameter).map(ClimateParameterDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public ClimateParameterDTO update(long id, ClimateParameterDTO parameter) {
        Optional<? extends ClimateParameter> l = repo.update(id, parameter);
        return l.map(ClimateParameterDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public ClimateParameterDTO get(long id) {
        return repo.get(id).map(ClimateParameterDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        repo.delete(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
