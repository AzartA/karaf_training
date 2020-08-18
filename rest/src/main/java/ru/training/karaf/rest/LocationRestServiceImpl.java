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
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.LocationDTO;
import ru.training.karaf.rest.validation.ErrorsDTO;
import ru.training.karaf.view.LocationView;

public class LocationRestServiceImpl implements LocationRestService {

    private LocationView view;

    public void setView(LocationView view) {
        this.view = view;
    }

    @Override
    public List<LocationDTO> getAll(
            List<String> by, List<String> order,
            List<String> field, List<String> cond, List<String> value, int pg, int sz,
            String login
    ) {
        return view.getAll(by, order, field, cond, value, pg, sz, login).map(l -> l.stream().map(LocationDTO::new).collect(Collectors.toList()))
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public DTO<Long> getCount(
            List<String> field, List<String> cond, List<String> value, int pg, int sz,
            String login
    ) {
        return (view.getCount(field, cond, value, pg, sz, login)).map(DTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public LocationDTO create(
            LocationDTO location,
            String login
    ) {
        return view.create(location, login).map(LocationDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public LocationDTO update(
            long id, LocationDTO location,
            String login
    ) {

        Optional<? extends Location> l = view.update(id, location, login);
        return l.map(LocationDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public LocationDTO get(
            long id,
            String login
    ) {
        return view.get(id, login).map(LocationDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(
            long id,
            String login
    ) {

        view.delete(id, login).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public Response getPlan(
            long id,
            String login
    ) {
        String type = view.get(id, login).map(Location::getPictureType)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
        StreamingOutput op = outputStream -> view.getPlan(id, outputStream, login)
                .orElseThrow(() -> new InternalServerErrorException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorsDTO("Can't get plan")).build()));
        return Response.ok(op).type(type).build();
    }

    @Override
    public DTO<Long> putPlan(
            long id, InputStream plan, String type,
            String login
    ) {
        LocationDTO location = view.get(id, login).map(LocationDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
        long size = view.setPlan(id, plan, type, login);
        if (size < 0) {
            throw new InternalServerErrorException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorsDTO("Can't put plan")).build());
        }
        return new DTO<>(size);
    }

    @Override
    public void deletePlan(
            long id,
            String login
    ) {
        view.get(id, login).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
        view.deletePlan(id, login).orElseThrow(() -> new InternalServerErrorException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorsDTO("Can't delete plan")).build()));
    }
}
