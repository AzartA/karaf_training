package ru.training.karaf.rest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import ru.training.karaf.model.User;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.FilterParamDTO;
import ru.training.karaf.rest.dto.SortParamDTO;
import ru.training.karaf.rest.dto.UserDTO;
import ru.training.karaf.view.FilterParam;
import ru.training.karaf.view.LocationView;
import ru.training.karaf.view.SortParam;
import ru.training.karaf.view.UserView;
import ru.training.karaf.view.ViewFacade;
import ru.training.karaf.view.ViewType;

import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserRestServiceImpl implements UserRestService {
    private DefaultPasswordService passwordService;
    private UserView view;
    private User currentUser;
    private ViewFacade viewFacade;

    public void setViewFacade(ViewFacade viewFacade) {
        this.viewFacade = viewFacade;
    }
    public void setPasswordService(DefaultPasswordService passwordService) {
        this.passwordService = passwordService;
    }

    private void getViewAndUser() {
        view = viewFacade.getView(UserView.class);
        currentUser = SecurityUtils.getSubject().getPrincipals().oneByType(User.class);
    }

    @Override
    public List<UserDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        getViewAndUser();
        List<FilterParam> filters = new ArrayList<>();
        List<SortParam> sorts = new ArrayList<>();

        for (int i = 0; i < field.size(); i++) {
            filters.add(new FilterParamDTO(field.get(i), cond.get(i), value.get(i), (view).getType()));
        }
        for (int i = 0; i < by.size(); i++) {
            sorts.add(new SortParamDTO(by.get(i), order.get(i), (view).getType()));
        }

        return view.getAll(filters, sorts, pg, sz, currentUser).stream().map(UserDTO::new).collect(Collectors.toList());
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
    public UserDTO create(UserDTO user) {
        getViewAndUser();
        user.setPassword(passwordService.encryptPassword(user.getPassword()));
        return view.create(user, currentUser).map(UserDTO::new).orElseThrow(() -> new ValidationException("login must be unique"));
    }

    @Override
    public UserDTO update(long id, UserDTO user) {
        getViewAndUser();
        user.setPassword(passwordService.encryptPassword(user.getPassword()));
        return view.update(id, user, currentUser).map(UserDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UserDTO get(long id) {
        getViewAndUser();
        return view.get(id, currentUser).map(UserDTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        getViewAndUser();
        view.delete(id, currentUser)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UserDTO addSensors(long id, List<Long> sensorIds) {
        getViewAndUser();
        return view.addSensors(id, sensorIds, currentUser).map(UserDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UserDTO addRoles(long id, List<Long> roleIds) {
        getViewAndUser();
        return view.addRoles(id, roleIds, currentUser).map(UserDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public UserDTO deleteRoles(long id, List<Long> roleIds) {
        getViewAndUser();
        return view.removeRoles(id, roleIds, currentUser).map(UserDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }


}
