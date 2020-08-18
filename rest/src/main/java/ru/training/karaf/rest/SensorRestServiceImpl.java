package ru.training.karaf.rest;

import ru.training.karaf.model.Sensor;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.SensorDTO;
import ru.training.karaf.view.SensorView;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SensorRestServiceImpl implements SensorRestService {
    private SensorView view;

    public void setView(SensorView view) {
        this.view = view;
    }

    @Override
    public List<SensorDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz,
                                 String login) {
        return view.getAll(by, order, field, cond, value, pg, sz, login).map(l-> l.stream().map(SensorDTO::new).collect(Collectors.toList()))
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz,
                         String login) {
        return (view.getCount(field, cond, value, pg, sz, login)).map(DTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO create(SensorDTO type,
                            String login) {
        return view.create(type, login).map(SensorDTO::new).orElseThrow(() -> new ForbiddenException(Response.status(Response.Status.FORBIDDEN).build()));
    }

    @Override
    public SensorDTO update(long id, SensorDTO type,
                            String login) {
        Optional<? extends Sensor> l = view.update(id, type, login);
        return l.map(SensorDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO setLocation(long id, long locationId,
                                 String login) {
        return view.setLocation(id, locationId, login).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO setSensorType(long id, long typeId,
                                   String login) {
        return view.setSensorType(id, typeId, login).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO addUsers(long id, List<Long> userIds,
                              String login) {
        return view.addUsers(id, userIds, login).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO setXY(long id, long x, long y,
                           String login) {
        return view.setXY(id, x, y, login).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO get(long id,
                         String login) {
        return view.get(id, login).map(SensorDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id,
                       String login) {
        view.delete(id, login).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
