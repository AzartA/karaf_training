package ru.training.karaf.rest;

import ru.training.karaf.rest.dto.SensorDTO;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import java.util.List;

@Path("sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface SensorRestService {
    @GET
    List<SensorDTO> getAll(@QueryParam("by") List<String> by,
                           @QueryParam("order") List<String> order,
                           @QueryParam("field") List<String> field,
                           @QueryParam("condition") List<String> cond,
                           @QueryParam("value") List<String> value,
                           @QueryParam("pg") int pg,
                           @QueryParam("sz") int sz);

    @GET
    @Path("count")
    long getCount(
            @QueryParam("field") List<String> field,
            @QueryParam("condition") List<String> cond,
            @QueryParam("value") List<String> value,
            @QueryParam("pg") int pg,
            @QueryParam("sz") int sz
    );

    @POST
    SensorDTO create(@Valid SensorDTO type);

    @PUT
    @Path("{id}")
    SensorDTO update(@PathParam("id") long id, @Valid SensorDTO type);

    @PUT
    @Path("{id}/location/{lId}")
    SensorDTO setLocation (@PathParam("id") long id, @PathParam("lId") long locationId);

    @PUT
    @Path("{id}/type/{tId}")
    SensorDTO setSensorType (@PathParam("id") long id, @PathParam("tId") long typeId);

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
