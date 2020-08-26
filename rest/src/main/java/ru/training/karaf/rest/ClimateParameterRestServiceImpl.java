package ru.training.karaf.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.rest.dto.ClimateParameterDTO;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.FilterParamDTO;
import ru.training.karaf.rest.dto.SortParamDTO;
import ru.training.karaf.rest.dto.UserDTO;
import ru.training.karaf.view.ClimateParameterView;
import ru.training.karaf.view.FilterParam;
import ru.training.karaf.view.LocationView;
import ru.training.karaf.view.SortParam;
import ru.training.karaf.view.ViewFacade;
import ru.training.karaf.view.ViewType;

public class ClimateParameterRestServiceImpl implements ClimateParameterRestService {
    private ViewFacade viewFacade;
    private ClimateParameterView view;
    private UserDTO currentUser;

    public void setViewFacade(ViewFacade viewFacade) {
        this.viewFacade = viewFacade;
    }

    private void getViewAndUser() {
        view = viewFacade.getView(ClimateParameterView.class);
        currentUser = SecurityUtils.getSubject().getPrincipals().oneByType(UserDTO.class);
    }

    @Override
    public List<ClimateParameterDTO> getAll(
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
        return view.getAll(filters, sorts, pg, sz, currentUser).stream().map(ClimateParameterDTO::new).collect(Collectors.toList());
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
    public ClimateParameterDTO create(
            ClimateParameterDTO parameter
    ) {
        getViewAndUser();
        return view.create(parameter, currentUser).map(ClimateParameterDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public ClimateParameterDTO update(
            long id, ClimateParameterDTO parameter
    ) {
        getViewAndUser();
        Optional<? extends ClimateParameter> l = view.update(id, parameter, currentUser);
        return l.map(ClimateParameterDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public ClimateParameterDTO addUnits(
            long id, List<Long> unitIds
    ) {
        getViewAndUser();
        return view.addUnits(id, unitIds, currentUser).map(ClimateParameterDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public ClimateParameterDTO get(long id) {
        getViewAndUser();
        return view.get(id, currentUser).map(ClimateParameterDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        getViewAndUser();
        view.delete(id, currentUser).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
