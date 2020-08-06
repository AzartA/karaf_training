package ru.training.karaf.rest;

import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.SensorType;
import ru.training.karaf.repo.SensorTypeRepo;
import ru.training.karaf.rest.dto.ClimateParameterDTO;
import ru.training.karaf.rest.dto.SensorTypeDTO;

import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SensorTypeRestServiceImpl implements SensorTypeRestService {
    private SensorTypeRepo repo;

    public void setRepo(SensorTypeRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<SensorTypeDTO> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue) {
        return repo.getAll(sortBy, sortOrder, pg, sz, filterField, filterValue)
                .stream().map(SensorTypeDTO::new).collect(Collectors.toList());
    }

    @Override
    public SensorTypeDTO create(SensorTypeDTO type) {
        return repo.create(type).map(SensorTypeDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public SensorTypeDTO update(long id, SensorTypeDTO type) {
        Optional<? extends SensorType> l = repo.update(id, type);
        return l.map(SensorTypeDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorTypeDTO addParameters(long id, List<Long> paramIds) {
        return repo.addParams(id, paramIds).map(SensorTypeDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorTypeDTO get(long id) {
        return repo.get(id).map(SensorTypeDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        repo.delete(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
