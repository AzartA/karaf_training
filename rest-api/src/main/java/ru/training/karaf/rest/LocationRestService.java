package ru.training.karaf.rest;

import ru.training.karaf.rest.dto.LocationDTO;

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
