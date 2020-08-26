package ru.training.karaf.rest.validation;

import ru.training.karaf.exception.RestrictedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestrictedExceptionMapper implements ExceptionMapper<RestrictedException> {
    @Override
    public Response toResponse(RestrictedException e) {
        return Response.status(Response.Status.FORBIDDEN).entity(new ErrorsDTO(e.getMessage())).build();
    }
}
