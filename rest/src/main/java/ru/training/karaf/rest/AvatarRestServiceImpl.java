package ru.training.karaf.rest;

import java.io.IOException;
import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import java.util.NoSuchElementException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.AvatarRepo;
import ru.training.karaf.repo.UserRepo;
import ru.training.karaf.rest.dto.AvatarDTO;
import ru.training.karaf.rest.dto.UserDTO;

public class AvatarRestServiceImpl implements AvatarRestService {

    private UserRepo userRepo;
    private AvatarRepo avatarRepo;
    

    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void setAvatarRepo(AvatarRepo avatarRepo) {
        this.avatarRepo = avatarRepo;
    }
    
    @Override
    public Response getAvatar(String libCard) {
        try {
            User user = userRepo.getUser(libCard).get();
            if (user.getAvatar() != null) {
                return Response
                        .ok(user.getAvatar().getPicture())
                        .header("Content-Disposition",
                            "attachment; filename=\"" + libCard + "-avatar.png\"")
                        .build();
            } else {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_PLAIN)
                        .entity("User doesn't have an avatar")
                        .build();
            }
        } catch (NoSuchElementException ex) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_PLAIN)
                        .entity("User not found")
                        .build();
        }
    }

    @Override
    public Response deleteAvatar(String libCard) {
        try {
            User user = userRepo.getUser(libCard).get();
            if (user.getAvatar() != null) {
                UserDTO userToUpdate = new UserDTO(user);
                userToUpdate.setAvatar(null);
                userRepo.updateUser(libCard, userToUpdate);
                //avatarRepo.deleteAvatar(user.getAvatar().getId());
                return Response
                        .ok("Successfully removed", MediaType.TEXT_PLAIN)
                        .build();
            } else {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_PLAIN)
                        .entity("User doesn't have an avatar")
                        .build();
            }
        } catch (NoSuchElementException ex) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_PLAIN)
                        .entity("User not found")
                        .build();
        }
    }
     
    @Override
    public Response uploadAvatar(String libCard, Attachment avatar) {
        try {
            User user = userRepo.getUser(libCard).get();
            InputStream attachment = avatar.getObject(InputStream.class);
            byte[] picture = new byte[attachment.available()];
            attachment.read(picture);
            UserDTO userToUpdate = new UserDTO(user);
            userToUpdate.setAvatar(new AvatarDTO(picture));
            userRepo.updateUser(libCard, userToUpdate);
        } catch(NoSuchElementException ex) {
            return Response
                        .status(Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_PLAIN)
                        .entity("User not found")
                        .build();
        } catch (IOException ex) {
            return Response
                        .status(Response.Status.INTERNAL_SERVER_ERROR)
                        .type(MediaType.TEXT_PLAIN)
                        .entity("An error occurred while uploading: " + ex)
                        .build();
        }
        return Response
                .ok("Successfully uploaded", MediaType.TEXT_PLAIN)
                .build();
    }
}
