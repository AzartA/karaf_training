package ru.training.karaf.rest;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.SensorTypeDTO;
import ru.training.karaf.rest.validation.CountParams;

@Path("types")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface SensorTypeRestService {
    @GET
    @CountParams
    List<SensorTypeDTO> getAll(
            @QueryParam("by") List<String> by,
            @QueryParam("order") List<String> order,
            @QueryParam("field") List<String> field,
            @QueryParam("condition") List<String> cond,
            @QueryParam("value") List<String> value,
            @Min(value = 1, message = "pg must be positive") @QueryParam("pg") int pg,
            @Min(value = 1, message = "sz must be positive") @QueryParam("sz") int sz
    );

    @GET
    @Path("count")
    @CountParams
    DTO<Long> getCount(
            @QueryParam("field") List<String> field,
            @QueryParam("condition") List<String> cond,
            @QueryParam("value") List<String> value,
            @Min(value = 0, message = "pg must be positive") @QueryParam("pg") int pg,
            @Min(value = 0, message = "sz must be positive") @QueryParam("sz") int sz
    );

    @POST
    SensorTypeDTO create(@Valid SensorTypeDTO type);

    @PUT
    @Path("{id}")
    SensorTypeDTO update(@PathParam("id") long id, @Valid SensorTypeDTO type);

    @PUT
    @Path("{id}/params")
    SensorTypeDTO addParameters(@PathParam("id") long id, @QueryParam("pId") List<Long> paramIds);

    @GET
    @Path("{id}")
    SensorTypeDTO get(@PathParam("id") long id);

    @DELETE
    @Path("{id}")
    void delete(@PathParam("id") long id);
}
