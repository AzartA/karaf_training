package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.BookDO;
import ru.training.karaf.model.GenreDO;

public class BookRepoImpl implements BookRepo {
    private JpaTemplate template;
    
    public BookRepoImpl(JpaTemplate template) {
        this.template = template;
    }
    
    public void init() {
        GenreDO genre = new GenreDO();
        genre.setName("g1");
        
        BookDO book = new BookDO();
        book.setAuthor("a1");
        book.setGenre(genre);
        book.setTitle("t1");
        book.setYear(2000);
        
        BookDO book1 = new BookDO();
        book1.setAuthor("a2");
        book1.setGenre(genre);
        book1.setTitle("t2");
        book1.setYear(1500);
        
        template.tx(em -> {
            em.persist(genre);
            em.persist(book);
            em.persist(book1);
        });
    }
    
    
    @Override
    public List<? extends Book> getAllBooks() {
        return template.txExpr(em -> em.createNamedQuery
            (BookDO.GET_ALL_BOOKS, BookDO.class).getResultList());
    }

    @Override
    public void createBook(Book book) {
        template.tx(em -> em.persist(book));
    }

    @Override
    public void updateBook(Book book) {
        template.tx(em -> em.merge(book));
    }

    @Override
    public Optional<? extends Book> getBook(String title) {
        return template.txExpr(em -> getByTitle(title, em));
    }

    @Override
    public void deleteBook(String title) {
        template.tx(em -> getByTitle(title, em).ifPresent(em::remove));
    }
    
    private Optional<BookDO> getByTitle(String title, EntityManager em) {
        try {
            return Optional.of(em.createNamedQuery(BookDO.GET_BOOK_BY_TITLE,
                    BookDO.class).setParameter("title", title)
                    .getSingleResult());
        } catch (NoResultException ex) {
            System.err.println("Book not found: " + ex);
            return Optional.empty();
        }
    }
}
