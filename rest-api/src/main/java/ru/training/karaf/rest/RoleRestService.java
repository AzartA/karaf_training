package ru.training.karaf.rest;

import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.RoleDTO;

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

@Path("roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RoleRestService {
    @GET
    List<RoleDTO> getAll(@QueryParam("by") List<String> by,
                           @QueryParam("order") List<String> order,
                           @QueryParam("field") List<String> field,
                           @QueryParam("condition") List<String> cond,
                           @QueryParam("value") List<String> value,
                           @QueryParam("pg") int pg,
                           @QueryParam("sz") int sz);

    @GET
    @Path("count")
    DTO<Long> getCount(
            @QueryParam("field") List<String> field,
            @QueryParam("condition") List<String> cond,
            @QueryParam("value") List<String> value,
            @QueryParam("pg") int pg,
            @QueryParam("sz") int sz
    );

    @POST
    RoleDTO create(@Valid RoleDTO type);

    @PUT
    @Path("{id}")
    RoleDTO update(@PathParam("id") long id, @Valid RoleDTO type);

    @PUT
    @Path("{id}/users")
    RoleDTO addUsers(@PathParam("id") long id, @QueryParam("uId") List<Long> userIds);

    @DELETE
    @Path("{id}/users")
    RoleDTO deleteUsers(@PathParam("id") long id, @QueryParam("uId") List<Long> userIds);

    @GET
    @Path("{id}")
    RoleDTO get(@PathParam("id") long id);

    @DELETE
    @Path("{id}")
    void delete(@PathParam("id") long id);
}
