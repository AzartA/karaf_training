package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.apache.aries.jpa.template.EmConsumer;
import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.BookDO;
import ru.training.karaf.model.Feedback;
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
        try {
            GenreDO genre = template.txExpr(em -> em.createNamedQuery
            (GenreDO.GET_GENRE_BY_NAME, GenreDO.class).setParameter("name", book.getGenre().getName()).getSingleResult());
            System.out.println("Genre: " + genre);
            BookDO bookToCreate = new BookDO(book);
            bookToCreate.setGenre(genre);
            template.tx(em -> em.merge(bookToCreate));
        } catch (NoResultException e) {
            System.err.println("Genre bot found: " + e);
        }
    }

    @Override
    public void updateBook(String title, Book book) {
        template.tx(em -> {
            getByTitle(title, em).ifPresent(bookToUpdate -> {
                bookToUpdate.setAuthor(book.getAuthor());
                bookToUpdate.getGenre().setName(book.getGenre().getName());
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

    @Override
    public List<? extends Feedback> getBookFeedbacks(String title) {
        Optional<BookDO> book = template.txExpr(em -> getByTitle(title, em));
        if (book.isPresent()) {
            return book.get().getFeedbacks();
        }
        return null;
    }    
}
