package ru.training.karaf.rest;

import ru.training.karaf.rest.dto.ClimateParameterDTO;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("params")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ClimateParameterRestService {
    @GET
    List<ClimateParameterDTO> getAll(
            @QueryParam("sortBy") String sortBy,
            @QueryParam("sortOrder") String sortOrder,
            @QueryParam("pg") int pg,
            @QueryParam("sz") int sz,
            @QueryParam("filterField") String filterField,
            @QueryParam("filterValue") String filterValue
    );

    @POST
    ClimateParameterDTO create(@Valid ClimateParameterDTO parameter);

    @PUT
    @Path("{id}")
    ClimateParameterDTO update(@PathParam("id") long id, @Valid ClimateParameterDTO parameter);

    @PUT
    @Path("{id}/units")
    ClimateParameterDTO addUnits(@PathParam("id") long id, @QueryParam("uId") List<Long> unitIds);

    @GET
    @Path("{id}")
    ClimateParameterDTO get(@PathParam("id") long id);

    @GET
    @Path("/name/{name}")
    ClimateParameterDTO getByName(@PathParam("name") String name);

    @DELETE
    @Path("{id}")
    void delete(@PathParam("id") long id);
}
