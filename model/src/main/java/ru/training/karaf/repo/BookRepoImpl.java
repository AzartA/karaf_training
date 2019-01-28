package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.BookDO;
import ru.training.karaf.model.FeedbackDO;
import ru.training.karaf.model.GenreDO;

public class BookRepoImpl implements BookRepo {
    private JpaTemplate template;
    
    public BookRepoImpl(JpaTemplate template) {
        this.template = template;
    }
    
    
    @Override
    public List<? extends Book> getAllBooks() {
        return template.txExpr(em -> em.createNamedQuery
            (BookDO.GET_ALL_BOOKS, BookDO.class).getResultList());
    }

    @Override
    public void createBook(Book book) {
        BookDO bookToCreate = new BookDO();
        bookToCreate.setAuthor(book.getAuthor());
        bookToCreate.setFeedbacks((List<FeedbackDO>)book.getFeedbacks());
        bookToCreate.setGenre((GenreDO)book.getGenre());
        bookToCreate.setTitle(book.getTitle());
        bookToCreate.setYear(book.getYear());
        
        template.tx(em -> em.persist(bookToCreate));
    }

    @Override
    public void updateBook(String title, Book book) {
        template.tx(em -> {
            getByTitle(title, em).ifPresent(bookToUpdate -> {
                bookToUpdate.setAuthor(book.getAuthor());
                bookToUpdate.setFeedbacks((List<FeedbackDO>)book.getFeedbacks());
                bookToUpdate.setGenre((GenreDO)book.getGenre());
                bookToUpdate.setTitle(book.getTitle());
                bookToUpdate.setYear(book.getYear());
                
                em.merge(bookToUpdate);
            });
        });
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
        } catch (NoResultException e) {
            System.err.println("Exception occurred while retrieving book"
                    + "from db: " + e);
            return Optional.empty();
        }
    }
    
}
