package ru.training.karaf.rest;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.rest.dto.GenreDTO;

@Path("genres")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GenreRestService {
    
    @GET
    List<GenreDTO> getAllGenres();
    
    @GET
    @Path("{name}")
    GenreDTO getGenre(@PathParam("name") String name);
    
    @POST
    Response createGenre(GenreDTO genre);
    
    @PUT
    @Path("{name}")
    Response updateGenre(@PathParam("name") String name, GenreDTO genre);
    
    @DELETE
    @Path("{name}")
    Response deleteGenre(@PathParam("name") String name);
    
}
