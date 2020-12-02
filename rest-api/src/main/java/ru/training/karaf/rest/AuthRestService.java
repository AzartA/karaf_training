package ru.training.karaf.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ru.training.karaf.rest.dto.AuthDTO;
import ru.training.karaf.rest.dto.UserDTO;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AuthRestService {

    @POST
    UserDTO setAuth(AuthDTO auth);
}
