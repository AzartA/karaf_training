package ru.training.karaf.rest;

import ru.training.karaf.model.User;
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
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserRestService {

    @GET
    List<UserDTO> getAll();

    @POST
    User create(@Valid UserDTO user);

    @PUT
    @Path("{login}")
    void update(@PathParam("login") String login, UserDTO user);

    @GET
    @Path("{login}")
    UserDTO get(@PathParam("login") String login);

    @DELETE
    @Path("{login}")
    void delete(@PathParam("login") String login);
}
