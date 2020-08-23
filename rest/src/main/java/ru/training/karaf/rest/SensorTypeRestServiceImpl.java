package ru.training.karaf.rest;

import ru.training.karaf.model.SensorType;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.SensorTypeDTO;
import ru.training.karaf.view.SensorTypeView;

import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SensorTypeRestServiceImpl implements SensorTypeRestService {
    private SensorTypeView view;

    public void setView(SensorTypeView view) {
        this.view = view;
    }

    @Override
    public List<SensorTypeDTO> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String login
    ) {
        return view.getAll(by, order, field, cond, value, pg, sz).stream().map(SensorTypeDTO::new).collect(Collectors.toList());
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz,
                              String login) {
        return new DTO<>(view.getCount(field, cond, value, pg, sz));
    }

    @Override
    public SensorTypeDTO create(SensorTypeDTO type,
                                String login) {
        return view.create(type).map(SensorTypeDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public SensorTypeDTO update(long id, SensorTypeDTO type,
                                String login) {
        Optional<? extends SensorType> l = view.update(id, type);
        return l.map(SensorTypeDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorTypeDTO addParameters(long id, List<Long> paramIds,
                                       String login) {
        return view.addParams(id, paramIds, login).map(SensorTypeDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorTypeDTO get(long id,
                             String login) {
        return view.get(id).map(SensorTypeDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id,
                       String login) {
        view.delete(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
