package ru.training.karaf.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import ru.training.karaf.model.Role;
import ru.training.karaf.model.User;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.FilterParamDTO;
import ru.training.karaf.rest.dto.RoleDTO;
import ru.training.karaf.rest.dto.SortParamDTO;
import ru.training.karaf.view.FilterParam;
import ru.training.karaf.view.RoleView;
import ru.training.karaf.view.SortParam;
import ru.training.karaf.view.ViewFacade;

public class RoleRestServiceImpl implements RoleRestService {
    private RoleView view;
    private User currentUser;
    private ViewFacade viewFacade;

    public void setViewFacade(ViewFacade viewFacade) {
        this.viewFacade = viewFacade;
    }

    private void getViewAndUser() {
        view = viewFacade.getView(RoleView.class);
        currentUser = SecurityUtils.getSubject().getPrincipals().oneByType(User.class);
    }

    @Override
    public RoleDTO create(RoleDTO role) {
        getViewAndUser();
        return view.create(role, currentUser).map(RoleDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public RoleDTO update(long id, RoleDTO type) {
        getViewAndUser();
        Optional<? extends Role> l = view.update(id, type, currentUser);
        return l.map(RoleDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO addUsers(long id, List<Long> userIds) {
        getViewAndUser();
        return view.addUsers(id, userIds, currentUser).map(RoleDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO deleteUsers(long id, List<Long> userIds) {
        getViewAndUser();
        return view.removeUsers(id, userIds, currentUser).map(RoleDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO get(long id) {
        getViewAndUser();
        return view.get(id, currentUser).map(RoleDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        view.delete(id, currentUser).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public List<RoleDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        getViewAndUser();
        List<FilterParam> filters = new ArrayList<>();
        List<SortParam> sorts = new ArrayList<>();

        for (int i = 0; i < field.size(); i++) {
            filters.add(new FilterParamDTO(field.get(i), cond.get(i), value.get(i), (view).getType()));
        }
        for (int i = 0; i < by.size(); i++) {
            sorts.add(new SortParamDTO(by.get(i), order.get(i), (view).getType()));
        }
        return view.getAll(filters, sorts, pg, sz, currentUser).stream().map(RoleDTO::new).collect(Collectors.toList());
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
