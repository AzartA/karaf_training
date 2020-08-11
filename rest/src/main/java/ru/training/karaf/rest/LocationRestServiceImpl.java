package ru.training.karaf.rest;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import ru.training.karaf.model.Location;
import ru.training.karaf.repo.LocationRepo;
import ru.training.karaf.rest.dto.LocationDTO;
import ru.training.karaf.rest.validation.ErrorsDTO;

public class LocationRestServiceImpl implements LocationRestService {

    private LocationRepo repo;

    public void setRepo(LocationRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<LocationDTO> getAll() {
        return repo.getAll().stream().map(LocationDTO::new).collect(Collectors.toList());
    }

    @Override
    public LocationDTO create(LocationDTO location) {
        return repo.create(location).map(LocationDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public LocationDTO update(long id, LocationDTO location) {

        Optional<? extends Location> l = repo.update(id, location);
        return l.map(LocationDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public LocationDTO get(long id) {
        return repo.get(id).map(LocationDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        repo.delete(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public Response getPlan(long id) {
        String type = repo.get(id).map(Location::getPictureType)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
        StreamingOutput op = outputStream -> repo.getPlan(id, outputStream);
        return Response.ok(op).type(type).build();
    }

    @Override
    public LocationDTO putPlan(long id, InputStream plan, String type) {
        LocationDTO location = repo.get(id).map(LocationDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
        long size = repo.setPlan(id, plan, type);
        if (size < 0) {
            throw new InternalServerErrorException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorsDTO("Can't put plan")).build());
        }
        location.setPictureSize(size);
        return location;
    }
}
