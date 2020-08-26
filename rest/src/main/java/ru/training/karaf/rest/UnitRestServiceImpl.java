package ru.training.karaf.rest;

import org.apache.shiro.SecurityUtils;
import ru.training.karaf.model.Unit;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.FilterParamDTO;
import ru.training.karaf.rest.dto.SortParamDTO;
import ru.training.karaf.rest.dto.UnitDTO;
import ru.training.karaf.rest.dto.UserDTO;
import ru.training.karaf.view.FilterParam;
import ru.training.karaf.view.LocationView;
import ru.training.karaf.view.SortParam;
import ru.training.karaf.view.UnitView;
import ru.training.karaf.view.UserView;
import ru.training.karaf.view.ViewFacade;
import ru.training.karaf.view.ViewType;

import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class UnitRestServiceImpl implements UnitRestService {
    private UserDTO currentUser;
    private UnitView view;
    private ViewFacade viewFacade;
    public void setViewFacade(ViewFacade viewFacade) {
        this.viewFacade = viewFacade;
    }
    private void getViewAndUser() {
        view = viewFacade.getView(UnitView.class);
        currentUser = SecurityUtils.getSubject().getPrincipals().oneByType(UserDTO.class);
    }


    @Override
    public UnitDTO create(UnitDTO unit) {
        getViewAndUser();
        return view.create(unit,currentUser ).map(UnitDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public UnitDTO update(long id, UnitDTO unit) {
        getViewAndUser();
        Optional<? extends Unit> ent = view.update(id, unit,currentUser );
        return ent.map(UnitDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UnitDTO get(long id) {
        getViewAndUser();
        return view.get(id,currentUser ).map(UnitDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        getViewAndUser();
        view.delete(id,currentUser ).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public List<UnitDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        view = viewFacade.getView(UnitView.class);
        getViewAndUser();
        List<FilterParam> filters = new ArrayList<>();
        List<SortParam> sorts = new ArrayList<>();

        for (int i = 0; i < field.size(); i++) {
            filters.add(new FilterParamDTO(field.get(i),cond.get(i),value.get(i),(view).getType()));
        }
        for (int i = 0; i < by.size(); i++) {
            sorts.add(new SortParamDTO(by.get(i),order.get(i),(view).getType()));
        }
        return view.getAll(filters, sorts,pg, sz,currentUser ).stream().map(UnitDTO::new).collect(Collectors.toList());
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        getViewAndUser();
        List<FilterParam> filters = new ArrayList<>();
        for (int i = 0; i < field.size(); i++) {
            filters.add(new FilterParamDTO(field.get(i),cond.get(i),value.get(i),(view).getType()));
        }
        return new DTO<>(view.getCount(filters, pg, sz, currentUser));
    }
}
