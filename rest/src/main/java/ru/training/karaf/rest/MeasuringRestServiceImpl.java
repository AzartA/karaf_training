package ru.training.karaf.rest;

import ru.training.karaf.model.Measuring;
import ru.training.karaf.repo.MeasuringRepo;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.MeasuringDTO;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MeasuringRestServiceImpl implements MeasuringRestService {
    private MeasuringRepo repo;

    public void setRepo(MeasuringRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<MeasuringDTO> getAll(List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        return repo.getAll(by, order, field, cond, value, pg, sz)
                .stream().map(MeasuringDTO::new).collect(Collectors.toList());
    }

    @Override
    public DTO<Long> getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        return new DTO<>(repo.getCount(field, cond, value, pg, sz));
    }

    @Override
    public MeasuringDTO create(MeasuringDTO type) {
        return repo.create(type).map(MeasuringDTO::new).orElse(null);
    }

    @Override
    public MeasuringDTO update(long id, MeasuringDTO type) {
        Optional<? extends Measuring> l = repo.update(id, type);
        return l.map(MeasuringDTO::new).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public MeasuringDTO get(long id) {
        return repo.get(id).map(MeasuringDTO::new).orElseThrow(
                () -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }

    @Override
    public void delete(long id) {
        repo.delete(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).build()));
    }
}
