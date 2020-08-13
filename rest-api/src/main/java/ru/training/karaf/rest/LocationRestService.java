package ru.training.karaf.rest;

import java.io.InputStream;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.LocationDTO;
import ru.training.karaf.rest.validation.PictureType;

@Path("locations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface LocationRestService {
    @GET
    List<LocationDTO> getAll(
            @QueryParam("by") List<String> by,
            @QueryParam("order") List<String> order,
            @QueryParam("field") List<String> field,
            @QueryParam("condition") List<String> cond,
            @QueryParam("value") List<String> value,
            @QueryParam("pg") int pg,
            @QueryParam("sz") int sz
    );

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
    LocationDTO create(@Valid LocationDTO location);

    @PUT
    @Path("{id}")
    LocationDTO update(@PathParam("id") long id, @Valid LocationDTO location);

    @GET
    @Path("{id}")
    LocationDTO get(@PathParam("id") long id);

    @DELETE
    @Path("{id}")
    void delete(@PathParam("id") long id);

    @GET
    @Path("{id}/plan")
    Response getPlan(@PathParam("id") long id);


    @POST
    @Path("{id}/plan")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    DTO<Long> putPlan(@PathParam("id") long id, @Multipart("plan") InputStream plan,
                @PictureType(message = "Picture type {type} isn't allowed.")
                                                  @Multipart("type") String type);

    @DELETE
    @Path("{id}/plan")
    void deletePlan(@PathParam("id") long id);


}
