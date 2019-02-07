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
                        new NotFoundException(
                                Response.status(Response.Status.NOT_FOUND)
                                        .type(MediaType.TEXT_PLAIN)
                                        .entity("Genre not found")
                                        .build()));
    }

    @Override
    public void createGenre(GenreDTO genre) {
        genreService.createGenre(genre);
    }

    @Override
    public void updateGenre(String name, GenreDTO genre) {
        genreService.updateGenre(name, genre);
    }

    @Override
    public void deleteGenre(String name) {
        genreService.deleteGenre(name);
    }
}
