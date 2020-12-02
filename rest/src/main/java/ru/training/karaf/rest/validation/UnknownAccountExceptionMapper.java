package ru.training.karaf.rest.validation;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.shiro.authc.UnknownAccountException;

public class UnknownAccountExceptionMapper implements ExceptionMapper<UnknownAccountException> {
    @Override
    public Response toResponse(UnknownAccountException e) {
        return Response.status(401).entity("error").build();
    }
}
