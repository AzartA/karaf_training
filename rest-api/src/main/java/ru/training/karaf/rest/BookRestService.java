package ru.training.karaf.rest;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.rest.dto.BookDTO;
import ru.training.karaf.rest.dto.FeedbackDTO;

@Path("books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface BookRestService {
    
    @GET
    List<BookDTO> getAllBooks();
    
    @GET
    @Path("{title}")        
    BookDTO getBook(@PathParam("title") String title);
    
    @POST
    Response createBook(BookDTO book);
    
    @PUT
    @Path("{title}")
    Response updateBook(@PathParam("title") String title, BookDTO book);
    
    @DELETE
    @Path("{title}")
    Response deleteBook(@PathParam("title") String title);
    
    @GET
    @Path("{title}/feedbacks")
    List<FeedbackDTO> getBookFeedbacks(@PathParam("title") String title);
    
}
