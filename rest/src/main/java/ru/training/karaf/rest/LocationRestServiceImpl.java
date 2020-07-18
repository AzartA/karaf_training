package ru.training.karaf.rest;

import ru.training.karaf.repo.LocationRepo;
import ru.training.karaf.rest.dto.LocationDTO;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

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
    public void create(LocationDTO location) {
        repo.create(location);
    }

    @Override
    public void update(String name, LocationDTO location) {
        repo.update(name, location);
    }

    @Override
    public LocationDTO get(String name) {
        return repo.get(name).map(LocationDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_HTML).entity("Location not found").build()));
    }

    @Override
    public void delete(String name) {
        repo.delete(name);
    }
}
