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

import org.apache.shiro.SecurityUtils;
import ru.training.karaf.model.Location;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.FilterParamDTO;
import ru.training.karaf.rest.dto.LocationDTO;
import ru.training.karaf.rest.dto.SortParamDTO;
import ru.training.karaf.rest.dto.UserDTO;
import ru.training.karaf.rest.validation.ErrorsDTO;
import ru.training.karaf.view.FilterParam;
import ru.training.karaf.view.LocationView;
import ru.training.karaf.view.MeasuringView;
import ru.training.karaf.view.SortParam;
import ru.training.karaf.view.ViewFacade;
import ru.training.karaf.view.ViewType;

public class LocationRestServiceImpl implements LocationRestService {
    private UserDTO currentUser;
    private LocationView view;
    private ViewFacade viewFacade;

    public void setViewFacade(ViewFacade viewFacade) {
        this.viewFacade = viewFacade;
    }

    private void getViewAndUser() {
        view =  viewFacade.getView(LocationView.class);
        currentUser = SecurityUtils.getSubject().getPrincipals().oneByType(UserDTO.class);
    }

    @Override
    public LocationDTO create(LocationDTO location) {
        getViewAndUser();
        return view.create(location, currentUser).map(LocationDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public LocationDTO update(long id, LocationDTO location) {
        getViewAndUser();
        Optional<? extends Location> l = view.update(id, location, currentUser);
        return l.map(LocationDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public LocationDTO get(long id) {
        getViewAndUser();
        return view.get(id, currentUser).map(LocationDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        getViewAndUser();
        view.delete(id, currentUser).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public Response getPlan(long id) {
        getViewAndUser();
        String type = view.get(id, currentUser).map(Location::getPictureType).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
        StreamingOutput op = outputStream -> view.getPlan(id, outputStream, currentUser).orElseThrow(
                () -> new InternalServerErrorException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorsDTO("Can't get plan"))
                        .build()));
        return Response.ok(op).type(type).build();
    }

    @Override
    public DTO<Long> putPlan(long id, InputStream plan, String type) {
        getViewAndUser();
        view.get(id, currentUser).map(LocationDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
        long size = view.setPlan(id, plan, type, currentUser);
        if (size < 0) {
            throw new InternalServerErrorException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorsDTO("Can't put plan"))
                    .build());
        }
        return new DTO<>(size);
    }

    @Override
    public void deletePlan(long id) {
        getViewAndUser();
        view.get(id, currentUser).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
        view.deletePlan(id, currentUser).orElseThrow(() -> new InternalServerErrorException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorsDTO("Can't delete plan")).build()));
    }

    @Override
    public List<LocationDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        getViewAndUser();
        List<FilterParam> filters = new ArrayList<>();
        List<SortParam> sorts = new ArrayList<>();

        for (int i = 0; i < field.size(); i++) {
            filters.add(new FilterParamDTO(field.get(i), cond.get(i), value.get(i), (view).getType()));
        }
        for (int i = 0; i < by.size(); i++) {
            sorts.add(new SortParamDTO(by.get(i), order.get(i), (view).getType()));
        }
        return view.getAll(filters, sorts, pg, sz, currentUser).stream().map(LocationDTO::new).collect(Collectors.toList());
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        getViewAndUser();
        List<FilterParam> filters = new ArrayList<>();
        for (int i = 0; i < field.size(); i++) {
            filters.add(new FilterParamDTO(field.get(i), cond.get(i), value.get(i), (view).getType()));
        }
        return new DTO<>(view.getCount(filters, pg, sz, currentUser));
    }
}
