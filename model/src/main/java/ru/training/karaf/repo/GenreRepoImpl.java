package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.Genre;
import ru.training.karaf.model.GenreDO;

public class GenreRepoImpl implements GenreRepo {
    private JpaTemplate template;

    public GenreRepoImpl(JpaTemplate template) {
        this.template = template;
    }
    
    public void init() {
        
       /* Default genre is used when the book doesn't have an actual genre
        * (e.g. genre was deleted). This is necessary in sake of non-nullable
        * constraint (book's genre cannot be be null) */
        
        GenreDO defaultGenre = new GenreDO();
        defaultGenre.setName(Genre.DEFAULT_GENRE);
        template.tx(em -> em.persist(defaultGenre));
    }

    @Override
    public List<? extends Genre> getAllGenres() {
        return template.txExpr(em -> em.createNamedQuery
            (GenreDO.GET_ALL_GENRES, GenreDO.class).getResultList());
    }

    @Override
    public void createGenre(Genre genre) {
        template.tx(em -> em.persist(genre));
    }

    @Override
    public void updateGenre(Genre genre) {
        template.tx(em -> em.merge(genre));
    }

    @Override
    public Optional<? extends Genre> getGenre(String name) {
        return template.txExpr(em -> getGenreByName(name, em));
    }

    @Override
    public void deleteGenre(String name) {
        template.tx(em -> getGenreByName(name, em).ifPresent(em::remove));
    }
    
    private Optional<GenreDO> getGenreByName(String name, EntityManager em) {
        try {
            return Optional.of(em.createNamedQuery(GenreDO.GET_GENRE_BY_NAME,
                    GenreDO.class)
                    .setParameter("name", name)
                    .getSingleResult());
        } catch (NoResultException e) {
            System.err.println("Genre not found: " + e);
            return Optional.empty();
        }
    }
}
