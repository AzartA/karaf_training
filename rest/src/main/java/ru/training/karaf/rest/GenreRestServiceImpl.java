package ru.training.karaf.rest;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.repo.GenreRepo;
import ru.training.karaf.rest.dto.GenreDTO;

public class GenreRestServiceImpl implements GenreRestService {
    
    private GenreRepo repo;
    
    public void setRepo(GenreRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<GenreDTO> getAllGenres() {
        return repo.getAllGenres()
                .stream()
                .map(g -> new GenreDTO(g))
                .collect(Collectors.toList());
    }

    @Override
    public GenreDTO getGenre(String name) {
        return repo.getGenre(name)
                .map(g -> new GenreDTO(g))
                .orElseThrow(() ->new NotFoundException
                    (Response.status(Response.Status.NOT_FOUND)
                            .type(MediaType.TEXT_PLAIN)
                            .entity("genre not found")
                            .build()));
    }

    @Override
    public void createGenre(GenreDTO genre) {
        repo.createGenre(genre);
    }

    @Override
    public void updateGenre(String name, GenreDTO genre) {
        repo.updateGenre(name, genre);
    }

    @Override
    public void deleteGenre(String name) {
        repo.deleteGenre(name);
    }
}
