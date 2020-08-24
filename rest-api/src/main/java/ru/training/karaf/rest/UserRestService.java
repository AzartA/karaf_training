package ru.training.karaf.rest;

import org.apache.cxf.jaxrs.ext.PATCH;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import ru.training.karaf.rest.dto.DTO;
import ru.training.karaf.rest.dto.RoleDTO;
import ru.training.karaf.rest.dto.SensorDTO;
import ru.training.karaf.rest.dto.UserDTO;
import ru.training.karaf.rest.validation.CountParams;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
import javax.ws.rs.core.StreamingOutput;
import java.io.InputStream;
import java.util.List;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserRestService {

    @GET
    @CountParams
    List<UserDTO> getAll(@QueryParam("by") List<String> by,
                         @QueryParam("order") List<String> order,
                         @QueryParam("field") List<String> field,
                         @QueryParam("condition") List<String> cond,
                         @QueryParam("value") List<String> value,
                         @Min(value = 0, message =  "pg must be positive")@QueryParam("pg") int pg,
                         @Min(value = 0, message =  "sz must be positive")@QueryParam("sz") int sz
    );

    @GET
    @Path("count")
    @CountParams
    DTO<Long> getCount(
            @QueryParam("field") List<String> field,
            @QueryParam("condition") List<String> cond,
            @QueryParam("value") List<String> value,
            @Min(value = 0, message =  "pg must be positive")@QueryParam("pg") int pg,
            @Min(value = 0, message =  "sz must be positive")@QueryParam("sz") int sz
    );

    @POST
    UserDTO create(@Valid UserDTO user);

    @PUT
    @Path("{id}")
    //@NotNull(message = "This login is already exist")
    UserDTO update(@PathParam("id") long id, @Valid  UserDTO user);


    @GET
    @Path("{id}")
    UserDTO get(@PathParam("id") long id);

    @DELETE
    @Path("{id}")
    void delete(@PathParam("id") long id);

    @PUT
    @Path("{id}/sensors")
    UserDTO addSensors(@PathParam("id") long id, @QueryParam("sId") List<Long> sensorIds);

    @PUT
    @Path("{id}/roles")
    UserDTO addRoles(@PathParam("id") long id, @QueryParam("rId") List<Long> roleIds);

    @DELETE
    @Path("{id}/roles")
    UserDTO deleteRoles(@PathParam("id") long id, @QueryParam("rId") List<Long> roleIds);

}
