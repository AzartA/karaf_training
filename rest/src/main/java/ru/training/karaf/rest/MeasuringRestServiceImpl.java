package ru.training.karaf.rest;

import ru.training.karaf.model.Measuring;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.MeasuringDTO;
import ru.training.karaf.view.MeasuringView;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MeasuringRestServiceImpl implements MeasuringRestService {
    private MeasuringView view;

    public void setView(MeasuringView view) {
        this.view = view;
    }

    @Override
    public List<MeasuringDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz,
                                     String login) {
        return view.getAll(by, order, field, cond, value, pg, sz).stream().map(MeasuringDTO::new).collect(Collectors.toList());
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz,
                              String login) {
        return new DTO<>(view.getCount(field, cond, value, pg, sz));
    }

    @Override
    public MeasuringDTO create(MeasuringDTO type,
                               String login) {
        return view.create(type).map(MeasuringDTO::new).orElse(null);
    }

    @Override
    public MeasuringDTO update(long id, MeasuringDTO type,
                               String login) {
        Optional<? extends Measuring> l = view.update(id, type);
        return l.map(MeasuringDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public MeasuringDTO get(long id,
                            String login) {
        return view.get(id).map(MeasuringDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id,
                       String login) {
        view.delete(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
