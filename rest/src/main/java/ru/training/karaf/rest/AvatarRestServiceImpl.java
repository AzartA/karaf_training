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
            throw new NotFoundException(
                    Response.status(Response.Status.NOT_FOUND)
                            .type(MediaType.TEXT_PLAIN)
                            .entity("Avatar not found")
                            .build());
        }
    }

    @Override
    public Response deleteAvatar(String libCard) {
        if (avatarService.deleteAvatar(libCard)) {
            return Response
                    .status(Response.Status.OK)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Avatar successfully deleted")
                    .build();
        } else {
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Cannot delete avatar")
                    .build();
        }
    }
     
    @Override
    public Response uploadAvatar(String libCard, Attachment avatar) {
        if (avatarService.uploadAvatar(libCard, avatar)) {
            return Response
                    .status(Response.Status.CREATED)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Avatar successfully uploaded")
                    .build();
        } else {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Cannot upload an avatar")
                    .build();
        }
    }
}
