package ru.training.karaf.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import ru.training.karaf.model.BookDO;
import ru.training.karaf.model.Genre;
import ru.training.karaf.model.GenreDO;
import ru.training.karaf.repo.BookRepo;
import ru.training.karaf.repo.GenreRepo;

public class GenreBuisnessLogicServiceImpl implements GenreBuisnessLogicService {
    
    private GenreRepo genreRepo;
    private BookRepo bookRepo;

    public void setGenreRepo(GenreRepo genreRepo) {
        this.genreRepo = genreRepo;
    }

    public void setBookRepo(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }
    
    public void init() {
        System.err.println("Genre service loaded");
    }

    @Override
    public List<? extends Genre> getAllGenres() {
        return genreRepo.getAllGenres();
    }

    @Override
    public Optional<? extends Genre> getGenre(String name) {
        return genreRepo.getGenre(name);
    }

    @Override
    public void createGenre(Genre genre) {
        if (!isGenreDataValid(genre)) {
            System.err.println("One or more parameters are invalid");
            return;
        }
        
        if (genreRepo.getGenre(genre.getName()).isPresent()) {
            System.err.println("Genre already exists");
            return;
        }
        
        GenreDO genreToCreate = new GenreDO();
        genreToCreate.setName(genre.getName());
        genreRepo.createGenre(genreToCreate);
    }

    @Override
    public void updateGenre(String name, Genre genre) {
        if (!isGenreDataValid(genre) || genre.getName().equals(Genre.DEFAULT_GENRE)) {
            System.err.println("One or more parameters are invalid");
            return;
        }
        
        if (genreRepo.getGenre(genre.getName()).isPresent()) {
            System.err.println("Genre already exists");
            return;
        }
        
        try {
            GenreDO genreToUpdate = (GenreDO)genreRepo.getGenre(name).get();
            genreToUpdate.setName(genre.getName());
            genreRepo.updateGenre(genreToUpdate);
        } catch (NoSuchElementException e) {
            System.err.println("Genre not found");
        }
    }

    @Override
    public void deleteGenre(String name) {
        if (name.equals(GenreDO.DEFAULT_GENRE)) {
            System.err.println("Access denied");
            return;
        }
        
        try {
            GenreDO genreToDelete = (GenreDO)genreRepo.getGenre(name).get();
            GenreDO defaultGenre = (GenreDO)genreRepo.getGenre(GenreDO.DEFAULT_GENRE).get();
            List<BookDO> books = (List<BookDO>)bookRepo.getAllBooks();
            books.forEach(b -> {
                if (b.getGenre().equals(genreToDelete)) {
                    b.setGenre(defaultGenre);
                    bookRepo.updateBook(b);
                }
            });
            genreRepo.deleteGenre(name);
        } catch (NoSuchElementException e) {
            System.err.println("Genre not found");
        }
    }
    
    private boolean isGenreDataValid(Genre genre) {
        return !(genre.getName() == null);
    }
}
