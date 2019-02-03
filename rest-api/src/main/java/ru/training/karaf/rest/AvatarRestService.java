package ru.training.karaf.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

@Path("/avatars")
public interface AvatarRestService {
    @GET
    @Produces("image/png")
    Response getAvatar(@QueryParam("libCard") String libCard);
    
    @POST
    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    Response uploadAvatar(@QueryParam("libCard") String libCard,
            @Multipart("avatar") Attachment avatar);
    
    @DELETE
    Response deleteAvatar(@QueryParam("libCard") String libCard);
}
