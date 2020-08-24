package ru.training.karaf.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import ru.training.karaf.model.Sensor;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.FilterParamDTO;
import ru.training.karaf.rest.dto.SensorDTO;
import ru.training.karaf.rest.dto.SortParamDTO;
import ru.training.karaf.view.FilterParam;
import ru.training.karaf.view.SensorView;
import ru.training.karaf.view.SortParam;

public class SensorRestServiceImpl implements SensorRestService {
    private SensorView view;
    @Valid
    private FilterParamDTO filterParam;
    @Valid
    private SortParamDTO sortParam;

    public void setView(SensorView view) {
        this.view = view;
    }

    @Override

    public List<SensorDTO> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz
    ) {
        List<FilterParam> filters = new ArrayList<>();
        List<SortParam> sorts = new ArrayList<>();

        for (int i = 0; i < field.size(); i++) {
            filterParam = new FilterParamDTO(field.get(i),cond.get(i),value.get(i), view.getType());
            filters.add(filterParam);
        }
        for (int i = 0; i < by.size(); i++) {
            sortParam = new SortParamDTO(by.get(i),order.get(i),view.getType());
            sorts.add(sortParam);
        }

        return view.getAll(filters, sorts, pg, sz).stream().map(SensorDTO::new).collect(Collectors.toList());
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        List<FilterParam> filters = new ArrayList<>();
        for (int i = 0; i < field.size(); i++) {
            filters.add(new FilterParamDTO(field.get(i),cond.get(i),value.get(i),view.getType()));
        }
        return new DTO<>(view.getCount(filters, pg, sz));
    }

    @Override
    public SensorDTO create(SensorDTO type) {
        return view.create(type).map(SensorDTO::new).orElseThrow(() -> new ForbiddenException(Response.status(Response.Status.FORBIDDEN).build()));
    }

    @Override
    public SensorDTO update(long id, SensorDTO type) {
        Optional<? extends Sensor> l = view.update(id, type);
        return l.map(SensorDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO setLocation(
            long id, long locationId
    ) {
        return view.setLocation(id, locationId).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO setSensorType(
            long id, long typeId
    ) {
        return view.setSensorType(id, typeId).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO addUsers(long id, List<Long> userIds) {
        return view.addUsers(id, userIds).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO setXY(long id, long x, long y) {
        return view.setXY(id, x, y).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO get(long id) {
        return view.get(id).map(SensorDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        view.delete(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
