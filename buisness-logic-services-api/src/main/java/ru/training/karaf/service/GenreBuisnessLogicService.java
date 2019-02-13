package ru.training.karaf.service;

import java.util.List;
import java.util.Optional;
import ru.training.karaf.model.Genre;

public interface GenreBuisnessLogicService {
    
    List<? extends Genre> getAllGenres();
    
    Optional<? extends Genre> getGenre(String name);
    
    boolean createGenre(Genre genre);
    
    boolean updateGenre(String name, Genre genre);
    
    boolean deleteGenre(String name);
    
}
