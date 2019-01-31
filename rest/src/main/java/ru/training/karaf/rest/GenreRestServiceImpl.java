package ru.training.karaf.rest;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.training.karaf.repo.BookRepo;
import ru.training.karaf.repo.GenreRepo;
import ru.training.karaf.rest.dto.GenreDTO;

public class GenreRestServiceImpl implements GenreRestService {
    
    private GenreRepo genreRepo;
    private BookRepo bookRepo;
    
    public void setGenreRepo(GenreRepo genreRepo) {
        this.genreRepo = genreRepo;
    }

    public void setBookRepo(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }
    
    @Override
    public List<GenreDTO> getAllGenres() {
        return genreRepo.getAllGenres()
                .stream()
                .map(g -> new GenreDTO(g))
                .collect(Collectors.toList());
    }

    @Override
    public GenreDTO getGenre(String name) {
        return genreRepo.getGenre(name)
                .map(g -> new GenreDTO(g))
                .orElseThrow(() ->
                        new NotFoundException(Response
                                .status(Response.Status.NOT_FOUND)
                                .type(MediaType.TEXT_PLAIN)
                                .entity("Genre not found")
                                .build()));
    }

    @Override
    public void createGenre(GenreDTO genre) {
        if (genreRepo.getGenre(genre.getName()).isPresent()) {
            throw new WebApplicationException(Response
                    .status(Response.Status.CONFLICT)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Genre already exists")
                    .build());
        }
        genreRepo.createGenre(genre);
    }

    @Override
    public void updateGenre(String name, GenreDTO genre) {
        if (genreRepo.getGenre(genre.getName()).isPresent()) {
            throw new WebApplicationException(Response
                    .status(Response.Status.CONFLICT)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Genre with specified name already exists")
                    .build());
        }
        genreRepo.updateGenre(name, genre);
    }

    @Override
    public void deleteGenre(String name) {
        bookRepo.getAllBooks().forEach(b -> {
            if (b.getGenre().getName().equals(name)) {
                bookRepo.deleteBook(b.getTitle());
            }
        });
        genreRepo.deleteGenre(name);
    }
}
