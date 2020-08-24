package ru.training.karaf.rest;

import ru.training.karaf.model.Measuring;
import ru.training.karaf.rest.dto.ClimateParameterDTO;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.FilterParamDTO;
import ru.training.karaf.rest.dto.MeasuringDTO;
import ru.training.karaf.rest.dto.SortParamDTO;
import ru.training.karaf.view.FilterParam;
import ru.training.karaf.view.MeasuringView;
import ru.training.karaf.view.SortParam;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MeasuringRestServiceImpl implements MeasuringRestService {
    private MeasuringView view;

    public void setView(MeasuringView view) {
        this.view = view;
    }



    @Override
    public MeasuringDTO create(MeasuringDTO type) {
        return view.create(type).map(MeasuringDTO::new).orElse(null);
    }

    @Override
    public MeasuringDTO update(long id, MeasuringDTO type) {
        Optional<? extends Measuring> l = view.update(id, type);
        return l.map(MeasuringDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public MeasuringDTO get(long id) {
        return view.get(id).map(MeasuringDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        view.delete(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public List<MeasuringDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        List<FilterParam> filters = new ArrayList<>();
        List<SortParam> sorts = new ArrayList<>();

        for (int i = 0; i < field.size(); i++) {
            filters.add(new FilterParamDTO(field.get(i),cond.get(i),value.get(i),view.getType()));
        }
        for (int i = 0; i < by.size(); i++) {
            sorts.add(new SortParamDTO(by.get(i),order.get(i),view.getType()));
        }
        return view.getAll(filters, sorts,pg, sz).stream().map(MeasuringDTO::new).collect(Collectors.toList());
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
