package ru.training.karaf.rest;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import java.util.NoSuchElementException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.service.AvatarBuisnessLogicService;

public class AvatarRestServiceImpl implements AvatarRestService {

    private AvatarBuisnessLogicService avatarService;

    public void setAvatarService(AvatarBuisnessLogicService avatarService) {
        this.avatarService = avatarService;
    }
    
    @Override
    public Response getAvatar(String libCard) {
        try {
            byte[] picture = avatarService.getAvatar(libCard).get();
                return Response
                        .ok(picture)
                        .header("Content-Disposition",
                            "attachment; filename=\"" + libCard + "-avatar.png\"")
                        .build();
        } catch (NoSuchElementException e) {
            throw new NotFoundException(buildResponse(
                    Response.Status.NOT_FOUND, "Avatar not found"));
        }
    }

    @Override
    public Response deleteAvatar(String libCard) {
        if (avatarService.deleteAvatar(libCard)) {
            return buildResponse(
                    Response.Status.OK, "Avatar successfully deleted");
        } else {
            return buildResponse(
                    Response.Status.NO_CONTENT, "Cannot delete avatar");
        }
    }
     
    @Override
    public Response uploadAvatar(String libCard, Attachment avatar) {
        if (avatarService.uploadAvatar(libCard, avatar)) {
            return buildResponse(
                    Response.Status.CREATED, "Avatar successfully uploaded");
        } else {
            return buildResponse(
                    Response.Status.INTERNAL_SERVER_ERROR, "Cannot upload an avatar");
        }
    }
    
    private Response buildResponse(Response.Status status, String desc) {
        return Response
                .status(status)
                .type(MediaType.TEXT_PLAIN)
                .entity(desc)
                .build();
    }
}
