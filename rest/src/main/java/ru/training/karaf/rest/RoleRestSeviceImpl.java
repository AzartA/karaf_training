package ru.training.karaf.rest;


import ru.training.karaf.model.Role;
import ru.training.karaf.rest.dto.ClimateParameterDTO;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.FilterParamDTO;
import ru.training.karaf.rest.dto.RoleDTO;
import ru.training.karaf.rest.dto.SortParamDTO;
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

public class RoleRestSeviceImpl implements  RoleRestService {
    private RoleView view;

    public void setView(RoleView view) {
        this.view = view;
    }



    @Override
    public RoleDTO create(RoleDTO role) {
        return view.create(role).map(RoleDTO::new).orElseThrow(() -> new ValidationException("Name is already exist"));
    }

    @Override
    public RoleDTO update(long id, RoleDTO type) {
        Optional<? extends Role> l = view.update(id, type);
        return l.map(RoleDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO addUsers(long id, List<Long> userIds) {
        return view.addUsers(id, userIds).map(RoleDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO deleteUsers(long id, List<Long> userIds) {
        return view.removeUsers(id, userIds).map(RoleDTO::new).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public RoleDTO get(long id) {
        return view.get(id).map(RoleDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        view.delete(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public List<RoleDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        List<FilterParam> filters = new ArrayList<>();
        List<SortParam> sorts = new ArrayList<>();

        for (int i = 0; i < field.size(); i++) {
            filters.add(new FilterParamDTO(field.get(i),cond.get(i),value.get(i),view.getType()));
        }
        for (int i = 0; i < by.size(); i++) {
            sorts.add(new SortParamDTO(by.get(i),order.get(i),view.getType()));
        }
        return view.getAll(filters, sorts,pg, sz).stream().map(RoleDTO::new).collect(Collectors.toList());
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
