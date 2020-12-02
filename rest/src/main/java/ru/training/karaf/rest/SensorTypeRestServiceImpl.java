package ru.training.karaf.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import ru.training.karaf.model.SensorType;
import ru.training.karaf.model.User;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.FilterParamDTO;
import ru.training.karaf.rest.dto.SensorTypeDTO;
import ru.training.karaf.rest.dto.SortParamDTO;
import ru.training.karaf.view.FilterParam;
import ru.training.karaf.view.SensorTypeView;
import ru.training.karaf.view.SortParam;
import ru.training.karaf.view.ViewFacade;

public class SensorTypeRestServiceImpl implements SensorTypeRestService {
    private SensorTypeView view;
    private User currentUser;
    private ViewFacade viewFacade;

    public void setViewFacade(ViewFacade viewFacade) {
        this.viewFacade = viewFacade;
    }

    private void getViewAndUser() {
        view = viewFacade.getView(SensorTypeView.class);
        currentUser = SecurityUtils.getSubject().getPrincipals().oneByType(User.class);
    }

    @Override
    public SensorTypeDTO create(SensorTypeDTO type) {
        getViewAndUser();
        return view.create(type, currentUser).map(SensorTypeDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public SensorTypeDTO update(long id, SensorTypeDTO type) {
        getViewAndUser();
        Optional<? extends SensorType> l = view.update(id, type, currentUser);
        return l.map(SensorTypeDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorTypeDTO addParameters(long id, List<Long> paramIds) {
        getViewAndUser();
        return view.addParams(id, paramIds, currentUser).map(SensorTypeDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public SensorTypeDTO get(long id) {
        getViewAndUser();
        return view.get(id, currentUser).map(SensorTypeDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        getViewAndUser();
        view.delete(id, currentUser).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public List<SensorTypeDTO> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz
    ) {
        getViewAndUser();
        List<FilterParam> filters = new ArrayList<>();
        List<SortParam> sorts = new ArrayList<>();

        for (int i = 0; i < field.size(); i++) {
            filters.add(new FilterParamDTO(field.get(i), cond.get(i), value.get(i), (view).getType()));
        }
        for (int i = 0; i < by.size(); i++) {
            sorts.add(new SortParamDTO(by.get(i), order.get(i), (view).getType()));
        }
        return view.getAll(filters, sorts, pg, sz, currentUser).stream().map(SensorTypeDTO::new).collect(Collectors.toList());
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
