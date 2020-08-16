package ru.training.karaf.rest;

import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.SensorTypeDTO;
import ru.training.karaf.rest.dto.SensorTypeDTO;

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


@Path("types")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface SensorTypeRestService {
    @GET
    List<SensorTypeDTO> getAll(@QueryParam("by") List<String> by,
                               @QueryParam("order") List<String> order,
                               @QueryParam("field") List<String> field,
                               @QueryParam("condition") List<String> cond,
                               @QueryParam("value") List<String> value,
                               @QueryParam("pg") int pg,
                               @QueryParam("sz") int sz,
                               @QueryParam("login") String login
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
        SensorTypeDTO create(@Valid SensorTypeDTO type,
                             @QueryParam("login") String login);

        @PUT
        @Path("{id}")
        SensorTypeDTO update(@PathParam("id") long id, @Valid SensorTypeDTO type,
                             @QueryParam("login") String login);

        @PUT
        @Path("{id}/params")
        SensorTypeDTO addParameters(@PathParam("id") long id, @QueryParam("pId") List<Long> paramIds,
                                    @QueryParam("login") String login);

        @GET
        @Path("{id}")
        SensorTypeDTO get(@PathParam("id") long id,
                          @QueryParam("login") String login);

        @DELETE
        @Path("{id}")
        void delete(@PathParam("id") long id,
                    @QueryParam("login") String login);
}
