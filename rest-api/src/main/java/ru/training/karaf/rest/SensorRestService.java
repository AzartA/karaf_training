package ru.training.karaf.rest;

import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.SensorDTO;
import ru.training.karaf.rest.validation.ConformingParams;
import ru.training.karaf.rest.validation.CountParams;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
import java.util.List;

@Path("sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface SensorRestService {
    String MIN_MSG = "The parameter must be positive";
    @GET
    @CountParams
    List<SensorDTO> getAll(
            @QueryParam("by") List<String> by,
            @QueryParam("order") List<String> order,
            @QueryParam("field") List<String> field,
            @QueryParam("condition") List<String> cond,
            @QueryParam("value") List<String> value,
            @Min(value = 0, message =  "pg must be positive") @QueryParam("pg") int pg,
            @Min(value = 0 , message = "sz must be positive") @QueryParam("sz") int sz
    );

    @GET
    @Path("count")
    @CountParams
    DTO<Long> getCount(
            @QueryParam("field") List<String> field,
            @QueryParam("condition") List<String> cond,
            @QueryParam("value") List<String> value,
            @Min(value = 0, message =  "pg must be positive")@QueryParam("pg") int pg,
            @Min(value = 0, message =  "sz must be positive")@QueryParam("sz") int sz
    );

    @POST
    SensorDTO create(@Valid SensorDTO type);

    @PUT
    @Path("{id}")
    SensorDTO update(@PathParam("id") long id, @Valid SensorDTO type);

    @PUT
    @Path("{id}/location/{lId}")
    SensorDTO setLocation(@PathParam("id") long id, @PathParam("lId") long locationId);

    @PUT
    @Path("{id}/type/{tId}")
    SensorDTO setSensorType(@PathParam("id") long id, @PathParam("tId") long typeId);

    @PUT
    @Path("{id}/users")
    SensorDTO addUsers(@PathParam("id") long id, @QueryParam("uId") List<Long> userIds);

    @PUT
    @Path("{id}/{x}/{y}")
    SensorDTO setXY(@PathParam("id") long id, @PathParam("x") long x, @PathParam("y") long y);

    @GET
    @Path("{id}")
    SensorDTO get(@PathParam("id") long id);

    @DELETE
    @Path("{id}")
    void delete(@PathParam("id") long id);
}
