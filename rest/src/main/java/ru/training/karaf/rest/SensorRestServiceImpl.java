package ru.training.karaf.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.Sensor;
import ru.training.karaf.repo.SensorRepo;
import ru.training.karaf.rest.dto.ClimateParameterDTO;
import ru.training.karaf.rest.dto.SensorDTO;

public class SensorRestServiceImpl implements SensorRestService {
    private SensorRepo repo;

    public void setRepo(SensorRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<SensorDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        return repo.getAll(by, order, field, cond, value, pg, sz)
                .stream().map(SensorDTO::new).collect(Collectors.toList());
    }

    @Override
    public SensorDTO create(SensorDTO type) {
        return repo.create(type).map(SensorDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public SensorDTO update(long id, SensorDTO type) {
        Optional<? extends Sensor> l = repo.update(id, type);
        return l.map(SensorDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO setLocation(long id, long locationId) {
        return repo.setLocation(id, locationId).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO setSensorType(long id, long typeId) {
        return repo.setSensorType(id, typeId).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO addUsers(long id, List<Long> userIds) {
        return repo.addUsers(id, userIds).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO get(long id) {
        return repo.get(id).map(SensorDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        repo.delete(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
