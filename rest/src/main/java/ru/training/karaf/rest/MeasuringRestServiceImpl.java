package ru.training.karaf.rest;

import ru.training.karaf.model.Measuring;
import ru.training.karaf.repo.MeasuringRepo;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.MeasuringDTO;
import ru.training.karaf.rest.dto.SensorDTO;
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
        return view.getAll(by, order, field, cond, value, pg, sz, login).map(l-> l.stream().map(MeasuringDTO::new).collect(Collectors.toList()))
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz,
                              String login) {
        return (view.getCount(field, cond, value, pg, sz, login)).map(DTO::new)
                .orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public MeasuringDTO create(MeasuringDTO type,
                               String login) {
        return view.create(type, login).map(MeasuringDTO::new).orElse(null);
    }

    @Override
    public MeasuringDTO update(long id, MeasuringDTO type,
                               String login) {
        Optional<? extends Measuring> l = view.update(id, type, login);
        return l.map(MeasuringDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public MeasuringDTO get(long id,
                            String login) {
        return view.get(id, login).map(MeasuringDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id,
                       String login) {
        view.delete(id, login).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
