package ru.training.karaf.rest;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.rest.dto.GenreDTO;
import ru.training.karaf.service.GenreBuisnessLogicService;

public class GenreRestServiceImpl implements GenreRestService {
    
    private GenreBuisnessLogicService genreService;

    public void setGenreService(GenreBuisnessLogicService genreService) {
        this.genreService = genreService;
    }
    
    @Override
    public List<GenreDTO> getAllGenres() {
        return genreService.getAllGenres()
                .stream()
                .map(g -> new GenreDTO(g))
                .collect(Collectors.toList());
    }

    @Override
    public GenreDTO getGenre(String name) {
        return genreService.getGenre(name)
                .map(g -> new GenreDTO(g))
                .orElseThrow(() ->
                        new NotFoundException(buildResponse(
                                Response.Status.NOT_FOUND, "Genre not found")));
    }

    @Override
    public Response createGenre(GenreDTO genre) {
        if (genreService.createGenre(genre)) {
            return buildResponse(Response.Status.CREATED,
                    "New genre successfully created");
        } else {
            return buildResponse(Response.Status.INTERNAL_SERVER_ERROR,
                    "Cannot create new gerne");
        }
    }

    @Override
    public Response updateGenre(String name, GenreDTO genre) {
        if (genreService.updateGenre(name, genre)) {
            return buildResponse(Response.Status.OK,
                    "Genre successfully updated");
        } else {
            return buildResponse(Response.Status.INTERNAL_SERVER_ERROR,
                    "Cannot update gerne");
        }
    }

    @Override
    public Response deleteGenre(String name) {
        if (genreService.deleteGenre(name)) {
            return buildResponse(Response.Status.OK,
                    "Gerne successfully deleted");
        } else {
            return buildResponse(Response.Status.INTERNAL_SERVER_ERROR,
                    "Cannot delete genre");
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
