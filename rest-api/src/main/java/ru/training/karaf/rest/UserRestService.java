package ru.training.karaf.rest;

import java.util.List;
import java.util.Set;
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
import javax.ws.rs.core.Response;
import ru.training.karaf.rest.dto.BookDTO;
import ru.training.karaf.rest.dto.FeedbackDTO;
import ru.training.karaf.rest.dto.UserDTO;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserRestService {
    
    @GET
    List<UserDTO> getAllUsers();
    
    @POST
    Response createUser(UserDTO user);
    
    @PUT
    @Path("{libCard}")
    Response updateUser(@PathParam("libCard") String libCard, UserDTO user);
    
    @GET
    @Path("{libCard}")
    UserDTO getUser(@PathParam("libCard") String libCard);
    
    @DELETE
    @Path("{libCard}")
    Response deleteUser(@PathParam("libCard") String libCard);
    
    @GET
    @Path("{libCard}/books")
    Set<BookDTO> getUserBooks(@PathParam("libCard") String libCard);
    
    @POST
    @Path("{libCard}/books")
    Response addBook(@PathParam("libCard") String libCard,
            @QueryParam("title") String title);
    
    @DELETE
    @Path("{libCard}/books")
    Response removeBook(@PathParam("libCard") String libCard,
            @QueryParam("title") String title);   
    
    @GET
    @Path("{libCard}/feedbacks")
    List<FeedbackDTO> getUserFeedbacks(@PathParam("libCard") String libCard);
    
    @POST
    @Path("{libCard}/feedbacks")
    Response addFeedback(@PathParam("libCard") String libCard,
            FeedbackDTO feedback);
    
    @DELETE
    @Path("{libCard}/feedbacks")
    Response removeFeedback(@PathParam("libCard") String libCard,
            @QueryParam("title") String title);
    
}
