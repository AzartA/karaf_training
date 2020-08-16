package ru.training.karaf.rest;

import ru.training.karaf.model.Unit;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.LocationDTO;
import ru.training.karaf.rest.dto.SensorDTO;
import ru.training.karaf.rest.dto.UnitDTO;
import ru.training.karaf.view.UnitView;

import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class UnitRestServiceImpl implements UnitRestService {

    private UnitView view;

    public void setView(UnitView view) {
        this.view = view;
    }

    @Override
    public List<UnitDTO> getAll(List<String> by, List<String> order,
                                    List<String> field, List<String> cond, List<String> value, int pg, int sz,
                                    String login) {
        return view.getAll(by, order, field, cond, value, pg, sz, login).map(l-> l.stream().map(UnitDTO::new).collect(Collectors.toList()))
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz,
                         String login) {
        return (view.getCount(field, cond, value, pg, sz, login)).map(DTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UnitDTO create(UnitDTO unit,
                          String login) {
        return view.create(unit, login).map(UnitDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public UnitDTO update(long id, UnitDTO unit,
                          String login) {
        Optional<? extends Unit> ent = view.update(id, unit, login);
        return ent.map(UnitDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UnitDTO get(long id,
                       String login) {
        return view.get(id, login).map(UnitDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id,
                       String login) {
        view.delete(id, login).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
