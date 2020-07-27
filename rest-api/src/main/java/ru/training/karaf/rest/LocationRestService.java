package ru.training.karaf.rest;

import ru.training.karaf.rest.dto.LocationDTO;
import ru.training.karaf.rest.dto.LocationDTO;

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

@Path("locations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface LocationRestService {
    @GET
    List<LocationDTO> getAll();

    @POST
    LocationDTO create(@Valid LocationDTO location);

    @PUT
    @Path("{id}")
    LocationDTO update(@PathParam("id") long id, @Valid  LocationDTO location);


    @GET
    @Path("{id}")
    LocationDTO get(@PathParam("id") long id);

    @DELETE
    @Path("{id}")
    void delete(@PathParam("id") long id);
}
