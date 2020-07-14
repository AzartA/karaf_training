package ru.training.karaf.rest;

import ru.training.karaf.rest.dto.LocationDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("locations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface LocationRestService {
    @GET
    List<LocationDTO> getAll();

    @POST
    void create(LocationDTO location);

    @PUT
    @Path("{name}")
    void update(@PathParam("name") String name, LocationDTO location);

    @GET
    @Path("{name}")
    LocationDTO get(@PathParam("name") String name);

    @DELETE
    @Path("{name}")
    void delete(@PathParam("name") String name);
}
