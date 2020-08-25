package ru.training.karaf.rest;


import org.apache.shiro.SecurityUtils;
import ru.training.karaf.model.Role;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.FilterParamDTO;
import ru.training.karaf.rest.dto.RoleDTO;
import ru.training.karaf.rest.dto.SortParamDTO;
import ru.training.karaf.rest.dto.UserDTO;
import ru.training.karaf.view.FilterParam;
import ru.training.karaf.view.RoleView;
import ru.training.karaf.view.SortParam;

import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleRestServiceImpl implements  RoleRestService {
    private RoleView view;
    private UserDTO currentUser;

    public void setView(RoleView view) {
        this.view = view;
    }



    @Override
    public RoleDTO create(RoleDTO role) {
        currentUser = SecurityUtils.getSubject().getPrincipals().oneByType(UserDTO.class);
        return view.create(role,currentUser ).map(RoleDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public RoleDTO update(long id, RoleDTO type) {
        currentUser = SecurityUtils.getSubject().getPrincipals().oneByType(UserDTO.class);
        Optional<? extends Role> l = view.update(id, type,currentUser );
        return l.map(RoleDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO addUsers(long id, List<Long> userIds) {
        currentUser = SecurityUtils.getSubject().getPrincipals().oneByType(UserDTO.class);
        return view.addUsers(id, userIds,currentUser ).map(RoleDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO deleteUsers(long id, List<Long> userIds) {
        currentUser = SecurityUtils.getSubject().getPrincipals().oneByType(UserDTO.class);
        return view.removeUsers(id, userIds,currentUser ).map(RoleDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO get(long id) {
        currentUser = SecurityUtils.getSubject().getPrincipals().oneByType(UserDTO.class);
        return view.get(id,currentUser ).map(RoleDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        view.delete(id,currentUser ).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public List<RoleDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        currentUser = SecurityUtils.getSubject().getPrincipals().oneByType(UserDTO.class);
        List<FilterParam> filters = new ArrayList<>();
        List<SortParam> sorts = new ArrayList<>();

        for (int i = 0; i < field.size(); i++) {
            filters.add(new FilterParamDTO(field.get(i),cond.get(i),value.get(i),view.getType()));
        }
        for (int i = 0; i < by.size(); i++) {
            sorts.add(new SortParamDTO(by.get(i),order.get(i),view.getType()));
        }
        return view.getAll(filters, sorts,pg, sz,currentUser ).stream().map(RoleDTO::new).collect(Collectors.toList());
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        currentUser = SecurityUtils.getSubject().getPrincipals().oneByType(UserDTO.class);
        List<FilterParam> filters = new ArrayList<>();
        for (int i = 0; i < field.size(); i++) {
            filters.add(new FilterParamDTO(field.get(i),cond.get(i),value.get(i),view.getType()));
        }
        return new DTO<>(view.getCount(filters, pg, sz,currentUser ));
    }
}
