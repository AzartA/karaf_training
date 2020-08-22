package ru.training.karaf.rest;

import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.SensorDTO;
import ru.training.karaf.rest.validation.ConformingParams;

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
    @GET
    @ConformingParams(message = "There is no such field, or condition is not applicable to the field's type")
    List<SensorDTO> getAll(
            @QueryParam("by") List<String> by,
            @Pattern(regexp = "^asc|desc$", message = "order must be asc or desc only")
            @QueryParam("order") List<String> order,
            @QueryParam("field") List<String> field,
            @Pattern(regexp = "^[><=]$|^[!<>]=$|^!?contains$", message = "{filter.condition.field}")
            @Size(min = 1, max = 9, message = "{filter.condition.field}")
            @QueryParam("condition") List<String> cond,
            @QueryParam("value") List<String> value,
            @Min(0) @QueryParam("pg") int pg,
            @Min(0) @QueryParam("sz") int sz
    );

    @GET
    @Path("count")
    DTO<Long> getCount(
            @QueryParam("field") List<String> field,
            @QueryParam("condition") List<String> cond,
            @QueryParam("value") List<String> value,
            @QueryParam("pg") int pg,
            @QueryParam("sz") int sz,
            @QueryParam("login") String login
    );

    @POST
    SensorDTO create(
            @Valid SensorDTO type,
            @QueryParam("login") String login
    );

    @PUT
    @Path("{id}")
    SensorDTO update(
            @PathParam("id") long id, @Valid SensorDTO type,
            @QueryParam("login") String login
    );

    @PUT
    @Path("{id}/location/{lId}")
    SensorDTO setLocation(
            @PathParam("id") long id, @PathParam("lId") long locationId,
            @QueryParam("login") String login
    );

    @PUT
    @Path("{id}/type/{tId}")
    SensorDTO setSensorType(
            @PathParam("id") long id, @PathParam("tId") long typeId,
            @QueryParam("login") String login
    );

    @PUT
    @Path("{id}/users")
    SensorDTO addUsers(
            @PathParam("id") long id, @QueryParam("uId") List<Long> userIds,
            @QueryParam("login") String login
    );

    @PUT
    @Path("{id}/{x}/{y}")
    SensorDTO setXY(
            @PathParam("id") long id, @PathParam("x") long x, @PathParam("y") long y,
            @QueryParam("login") String login
    );

    @GET
    @Path("{id}")
    SensorDTO get(
            @PathParam("id") long id,
            @QueryParam("login") String login
    );

    @DELETE
    @Path("{id}")
    void delete(
            @PathParam("id") long id,
            @QueryParam("login") String login
    );
}
