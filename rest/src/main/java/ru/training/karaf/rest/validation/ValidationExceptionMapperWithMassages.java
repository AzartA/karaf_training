package ru.training.karaf.rest.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.jaxrs.validation.ValidationExceptionMapper;
import org.apache.cxf.validation.ResponseConstraintViolationException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ValidationExceptionMapperWithMassages extends ValidationExceptionMapper {
    private static final Logger LOG = LogUtils.getL7dLogger(ValidationExceptionMapper.class);

    @Override
    public Response toResponse(ValidationException exception) {

        List<String> responseText = new ArrayList<>();

        Response.Status errorStatus = Response.Status.INTERNAL_SERVER_ERROR;
        if (exception instanceof ConstraintViolationException) {
            final ConstraintViolationException constraint = (ConstraintViolationException) exception;

                responseText.addAll(constraint.getConstraintViolations().stream()
                        .map(violation -> {
                            String message = buildErrorMessage(violation);
                            LOG.warning(message);
                            return message;
                         }).collect(Collectors.toList()));

            if (!(constraint instanceof ResponseConstraintViolationException)) {
                errorStatus = Response.Status.BAD_REQUEST;
            }
            return buildResponse(errorStatus, responseText);
        }
        responseText.add(exception.getMessage());

        return buildResponse(errorStatus, responseText);
    }

    private Response buildResponse(Response.Status errorStatus, List<String> responseText) {
        ResponseBuilder rb = JAXRSUtils.toResponseBuilder(errorStatus);
        if (responseText != null) {
            ObjectMapper mapper = new ObjectMapper();
            try{
                rb.type(MediaType.APPLICATION_JSON).entity("{\"errors\":"+ mapper.writeValueAsString(responseText) +"}");

            } catch ( com.fasterxml.jackson.core.JsonProcessingException e) {
                throw new IllegalArgumentException("Unable serialize response message to json", e);
            }
        }
        return rb.build();
    }

    private String buildErrorMessage(ConstraintViolation<?> violation) {
        return /*"Value "
                + (violation.getInvalidValue() != null ? "'" + violation.getInvalidValue().toString() + "'" : "(null)")
                + " of " + violation.getRootBeanClass().getSimpleName()
                + "." + violation.getPropertyPath()
                + ": " + */
                violation.getMessage();
    }
}
