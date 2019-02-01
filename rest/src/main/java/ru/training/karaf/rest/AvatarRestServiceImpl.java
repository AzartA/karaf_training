package ru.training.karaf.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import java.util.NoSuchElementException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.UserRepo;
import ru.training.karaf.rest.dto.AvatarDTO;
import ru.training.karaf.rest.dto.UserDTO;

public class AvatarRestServiceImpl implements AvatarRestService {

    private UserRepo repo;

    public void setRepo(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public Response getAvatar(String libCard) {
        try {
            User user = repo.getUser(libCard).get();
            return Response
                    .ok(user.getAvatar().getPicture())
                    .header("Content-Disposition", "attachment; filename=\"avatar.png\"")
                    .build();
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
            User user = repo.getUser(libCard).get();
            System.err.println("Avatar: " + avatar);
            InputStream attachment = avatar.getObject(InputStream.class);
            byte[] picture = new byte[attachment.available()];
            attachment.read(picture);
            UserDTO userToUpdate = new UserDTO(user);
            System.err.println("Before: " +
                    Arrays.toString(userToUpdate.getAvatar().getPicture()));
            userToUpdate.setAvatar(new AvatarDTO(picture));
            System.err.println("After: " +
                    Arrays.toString(userToUpdate.getAvatar().getPicture()));
            repo.updateUser(libCard, userToUpdate);
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
