package ru.training.karaf.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.User;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.FilterParamDTO;
import ru.training.karaf.rest.dto.SensorDTO;
import ru.training.karaf.rest.dto.SortParamDTO;
import ru.training.karaf.view.FilterParam;
import ru.training.karaf.view.SensorView;
import ru.training.karaf.view.SortParam;
import ru.training.karaf.view.ViewFacade;

public class SensorRestServiceImpl implements SensorRestService {
    private ViewFacade viewFacade;
    private SensorView view;
    private User currentUser;
    @Valid
    private FilterParamDTO filterParam;
    @Valid
    private SortParamDTO sortParam;

    public void setViewFacade(ViewFacade viewFacade) {
        this.viewFacade = viewFacade;
    }

    private void getViewAndUser() {
        view = viewFacade.getView(SensorView.class);
        currentUser = SecurityUtils.getSubject().getPrincipals().oneByType(User.class);
    }

    @Override

    public List<SensorDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        getViewAndUser();
        List<FilterParam> filters = new ArrayList<>();
        List<SortParam> sorts = new ArrayList<>();

        for (int i = 0; i < field.size(); i++) {
            filterParam = new FilterParamDTO(field.get(i), cond.get(i), value.get(i), (view).getType());
            filters.add(filterParam);
        }
        for (int i = 0; i < by.size(); i++) {
            sortParam = new SortParamDTO(by.get(i), order.get(i), (view).getType());
            sorts.add(sortParam);
        }

        return view.getAll(filters, sorts, pg, sz, currentUser).stream().map(SensorDTO::new).collect(Collectors.toList());
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

    @Override
    public SensorDTO create(SensorDTO type) {
        getViewAndUser();
        return view.create(type, currentUser).map(SensorDTO::new).orElseThrow(
                () -> new ForbiddenException(Response.status(Response.Status.FORBIDDEN).build()));
    }

    @Override
    public SensorDTO update(long id, SensorDTO type) {
        getViewAndUser();
        Optional<? extends Sensor> l = view.update(id, type, currentUser);
        return l.map(SensorDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO setLocation(long id, long locationId) {
        getViewAndUser();
        return view.setLocation(id, locationId, currentUser).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO setSensorType(long id, long typeId) {
        getViewAndUser();
        return view.setSensorType(id, typeId, currentUser).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO addUsers(long id, List<Long> userIds) {
        getViewAndUser();
        return view.addUsers(id, userIds, currentUser).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO setXY(long id, long x, long y) {
        getViewAndUser();
        return view.setXY(id, x, y, currentUser).map(SensorDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorDTO get(long id) {
        getViewAndUser();
        return view.get(id, currentUser).map(SensorDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        getViewAndUser();
        view.delete(id, currentUser).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
