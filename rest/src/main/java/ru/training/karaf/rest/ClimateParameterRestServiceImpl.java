package ru.training.karaf.rest;

import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.repo.ClimateParameterRepo;
import ru.training.karaf.rest.dto.ClimateParameterDTO;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.SensorDTO;
import ru.training.karaf.view.ClimateParameterView;

import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClimateParameterRestServiceImpl implements ClimateParameterRestService {
    private ClimateParameterView view;

    public void setView(ClimateParameterView view) {
        this.view = view;
    }

    @Override
    public List<ClimateParameterDTO> getAll(List<String> by, List<String> order,
                                            List<String> field, List<String> cond, List<String> value, int pg, int sz,
                                            String login) {
        return view.getAll(by, order, field, cond, value, pg, sz, login).map(l-> l.stream().map(ClimateParameterDTO::new).collect(Collectors.toList()))
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz,
                              String login) {
        return (view.getCount(field, cond, value, pg, sz, login)).map(DTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public ClimateParameterDTO create(ClimateParameterDTO parameter,
                                      String login) {
        return view.create(parameter, login).map(ClimateParameterDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public ClimateParameterDTO update(long id, ClimateParameterDTO parameter,
                                      String login) {
        Optional<? extends ClimateParameter> l = view.update(id, parameter, login);
        return l.map(ClimateParameterDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public ClimateParameterDTO addUnits(long id, List<Long> unitIds,
                                        String login) {
        return view.addUnits(id, unitIds, login).map(ClimateParameterDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public ClimateParameterDTO get(long id,
                                   String login) {
        return view.get(id, login).map(ClimateParameterDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public ClimateParameterDTO getByName(String name,
                                         String login) {
        return view.getByName(name, login).map(ClimateParameterDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id,
                       String login) {
        view.delete(id, login).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
