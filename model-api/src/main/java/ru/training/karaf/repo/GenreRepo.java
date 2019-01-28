package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import ru.training.karaf.model.Genre;

public interface GenreRepo {
    List<? extends Genre> getAllGenres();
    void createGenre(Genre genre);
    void updateGenre(String name, Genre genre);
    Optional<? extends Genre> getGenre(String name);
    void deleteGenre(String name);
}
