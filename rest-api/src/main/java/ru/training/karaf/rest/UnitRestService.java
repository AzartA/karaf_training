package ru.training.karaf.rest;

import java.util.List;
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

import ru.training.karaf.rest.dto.UnitDTO;

@Path("units")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UnitRestService {
    @GET
    List<UnitDTO> getAll();

    @POST
    UnitDTO create(@Valid UnitDTO entity);

    @PUT
    @Path("{id}")
    UnitDTO update(@PathParam("id") long id, @Valid UnitDTO entity);

    @GET

    @Path("{id}")
    UnitDTO get(@PathParam("id") long id);

    @DELETE
    @Path("{id}")
    void delete(@PathParam("id") long id);
}
