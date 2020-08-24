package ru.training.karaf.rest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import ru.training.karaf.model.Location;
import ru.training.karaf.rest.dto.ClimateParameterDTO;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.FilterParamDTO;
import ru.training.karaf.rest.dto.LocationDTO;
import ru.training.karaf.rest.dto.SortParamDTO;
import ru.training.karaf.rest.validation.ErrorsDTO;
import ru.training.karaf.view.FilterParam;
import ru.training.karaf.view.LocationView;
import ru.training.karaf.view.SortParam;

public class LocationRestServiceImpl implements LocationRestService {

    private LocationView view;

    public void setView(LocationView view) {
        this.view = view;
    }



    @Override
    public LocationDTO create(
            LocationDTO location
    ) {
        return view.create(location).map(LocationDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public LocationDTO update(
            long id, LocationDTO location
    ) {

        Optional<? extends Location> l = view.update(id, location);
        return l.map(LocationDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public LocationDTO get(
            long id
    ) {
        return view.get(id).map(LocationDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(
            long id
    ) {

        view.delete(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public Response getPlan(
            long id
    ) {
        String type = view.get(id).map(Location::getPictureType)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
        StreamingOutput op = outputStream -> view.getPlan(id, outputStream)
                .orElseThrow(() -> new InternalServerErrorException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorsDTO("Can't get plan")).build()));
        return Response.ok(op).type(type).build();
    }

    @Override
    public DTO<Long> putPlan(
            long id, InputStream plan, String type
    ) {
        LocationDTO location = view.get(id).map(LocationDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
        long size = view.setPlan(id, plan, type);
        if (size < 0) {
            throw new InternalServerErrorException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorsDTO("Can't put plan")).build());
        }
        return new DTO<>(size);
    }

    @Override
    public void deletePlan(
            long id
    ) {
        view.get(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
        view.deletePlan(id).orElseThrow(() -> new InternalServerErrorException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorsDTO("Can't delete plan")).build()));
    }

    @Override
    public List<LocationDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        List<FilterParam> filters = new ArrayList<>();
        List<SortParam> sorts = new ArrayList<>();

        for (int i = 0; i < field.size(); i++) {
            filters.add(new FilterParamDTO(field.get(i),cond.get(i),value.get(i),view.getType()));
        }
        for (int i = 0; i < by.size(); i++) {
            sorts.add(new SortParamDTO(by.get(i),order.get(i),view.getType()));
        }
        return view.getAll(filters, sorts,pg, sz).stream().map(LocationDTO::new).collect(Collectors.toList());
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        List<FilterParam> filters = new ArrayList<>();
        for (int i = 0; i < field.size(); i++) {
            filters.add(new FilterParamDTO(field.get(i),cond.get(i),value.get(i),view.getType()));
        }
        return new DTO<>(view.getCount(filters, pg, sz));
    }
}
