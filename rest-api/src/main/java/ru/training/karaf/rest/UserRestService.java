package ru.training.karaf.rest;

import ru.training.karaf.rest.dto.SensorDTO;
import ru.training.karaf.rest.dto.UserDTO;

import javax.validation.Valid;
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

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserRestService {

    @GET
    List<UserDTO> getAll(@QueryParam("by") List<String> by,
                         @QueryParam("order") List<String> order,
                         @QueryParam("field") List<String> field,
                         @QueryParam("condition") List<String> cond,
                         @QueryParam("value") List<String> value,
                         @QueryParam("pg") int pg,
                         @QueryParam("sz") int sz);

    @POST
    UserDTO create(@Valid UserDTO user);

    @PUT
    @Path("{id}")
    //@NotNull(message = "This login is already exist")
    UserDTO update(@PathParam("id") long id, @Valid  UserDTO user);


    @GET
    @Path("login/{login}")
    UserDTO getByLogin(@PathParam("login") String login);

    @GET
    @Path("{id}")
    UserDTO get(@PathParam("id") long id);

    @DELETE
    @Path("{id}")
    void delete(@PathParam("id") long id);

    @PUT
    @Path("{id}/sensors")
    UserDTO addSensors(@PathParam("id") long id, @QueryParam("sId") List<Long> sensorIds);
}
