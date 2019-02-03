package ru.training.karaf.repo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.AvatarDO;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.BookDO;
import ru.training.karaf.model.Feedback;
import ru.training.karaf.model.FeedbackDO;
import ru.training.karaf.model.GenreDO;
import ru.training.karaf.model.User;
import ru.training.karaf.model.UserDO;
import ru.training.karaf.model.UserNameDO;

public class UserRepoImpl implements UserRepo {
    private JpaTemplate template;

    public UserRepoImpl(JpaTemplate template) {
        this.template = template;
    }

    public void init() throws IOException {
        InputStream in = new FileInputStream("C:\\PROJECTS\\Java_Projects\\karaf_training\\a.png");
        byte[] picture = new byte[in.available()];
        in.read(picture);
        
        AvatarDO avatar = new AvatarDO();
        avatar.setPicture(picture);
        
        UserNameDO name = new UserNameDO();
        name.setFirstName("afn");
        name.setLastName("aln");
        
        String stringAddress = "{\"country\":\"russia\",\"city\":\"moscow\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode address = mapper.readTree(stringAddress);
               
        FeedbackDO feedback = new FeedbackDO();
        feedback.setMessage("af");
        
        GenreDO genre = new GenreDO();
        genre.setName("ag");

        BookDO book = new BookDO();
        book.setAuthor("aba");
        book.setGenre(genre);
        book.setTitle("abt");
        book.setYear(2000);
        book.addFeedback(feedback);
        Set<BookDO> books = new HashSet<>();
        books.add(book);
        
        UserDO admin = new UserDO();
        admin.setAddress(address);
        admin.setLibCard("alc");
        admin.setRegDate(new Date());
        admin.setAvatar(avatar);
        admin.setUserName(name);
        admin.addFeedback(feedback);
        admin.setBooks(books);

        template.tx(em -> em.persist(genre));
        template.tx(em -> em.persist(admin));
    }
    
    @Override
    public List<? extends User> getAllUsers() {
        return template.txExpr(em -> em.createNamedQuery
            (UserDO.GET_ALL_USERS, UserDO.class).getResultList());
    }
    
    @Override
    public void createUser(User user) {
        UserDO userToCreate = new UserDO(user);
        template.tx(em -> em.persist(userToCreate));
    }

    @Override
    public void updateUser(String libCard, User user) {
        template.tx(em -> {
            getUserByLibCard(libCard, em).ifPresent(userToUpdate -> {
                userToUpdate.setAddress(user.getAddress());
                // Avatar is not presented
                if (userToUpdate.getAvatar() != null) {
                    userToUpdate.getAvatar().setPicture(user.getAvatar().getPicture());
                }
                userToUpdate.setLibCard(user.getLibCard());
                userToUpdate.setRegDate(user.getRegDate());
                userToUpdate.setUserName(new UserNameDO(user.getUserName()));
                Set<BookDO> temp = new HashSet<>();
                for (Book b: user.getBooks()) {
                    temp.add(em.createNamedQuery(BookDO.GET_BOOK_BY_TITLE,
                            BookDO.class)
                            .setParameter("title", b.getTitle())
                            .getSingleResult());
                }
//                userToUpdate.setBooks(user
//                        .getBooks()
//                        .stream()
//                        .map(b ->
//                        new BookDO(b))
//                        .collect(Collectors.toSet()));
                userToUpdate.setBooks(temp);
                
//                userToUpdate.getBooks().forEach(b -> em.createNamedQuery(
//                            BookDO.GET_BOOK_BY_TITLE, BookDO.class)
//                            .setParameter("title", b.getTitle())
//                            .getSingleResult());
//                userToUpdate.getBooks().forEach(b -> {
//                    b.setGenre(em.createNamedQuery(
//                                        GenreDO.GET_GENRE_BY_NAME,
//                                        GenreDO.class)
//                                .setParameter("name", b.getGenre().getName())
//                                .getSingleResult());
//                });
//                System.err.println("updateUser: " + user.getBooks());
//                for (Book b: user.getBooks()) {
//                    BookDO book = em.createNamedQuery(
//                            BookDO.GET_BOOK_BY_TITLE, BookDO.class)
//                            .setParameter("title", b.getTitle())
//                            .getSingleResult();
//                    userToUpdate.getBooks().add(book);
//                }
                //System.err.println("userToUpdate: " + userToUpdate.getBooks().size());
                em.merge(userToUpdate);
            });
        });
    }

    @Override
    public Optional<? extends User> getUser(String libCard) {
        return template.txExpr(em -> getUserByLibCard(libCard, em));
    }

    @Override
    public void deleteUser(String libCard) {
        template.tx(em -> getUserByLibCard(libCard, em).ifPresent(em::remove));
    }

//    @Override
//    public Set<? extends Book> getUserBooks(String libCard) {
//        Optional<UserDO> user = template.txExpr(em -> getUserByLibCard(libCard, em));
//        if (user.isPresent()) {
//            return user.get().getBooks();
//        }
//        return null;
//    }

//    @Override
//    public void addBook(String libCard, Book requestedBook) {
//        try {
//            BookDO book = template.txExpr(em ->
//                    em.createNamedQuery(BookDO.GET_BOOK_BY_TITLE, BookDO.class)
//                            .setParameter("title", requestedBook.getTitle())
//                            .getSingleResult());
//            
//            UserDO user = template.txExpr(em -> getUserByLibCard(libCard, em)).get();
//            user.getBooks().add(book);
//            template.tx(em -> em.merge(user));
//            
//        } catch (NoResultException ex) {
//            System.err.println("Book not found: " + ex);
//        } catch (NoSuchElementException ex) {
//            System.err.println("User not found: " + ex);
//        }
//    }

//    @Override
//    public void removeBook(String libCard, String title) {
//        try {
//            BookDO book = template.txExpr(em ->
//                    em.createNamedQuery(BookDO.GET_BOOK_BY_TITLE, BookDO.class)
//                            .setParameter("title", title)
//                            .getSingleResult());
//            
//            UserDO user = template.txExpr(em -> getUserByLibCard(libCard, em)).get();
//            if (user.getBooks().remove(book)) {
//                template.tx(em -> em.merge(user));
//            } else {
//                System.out.println("User doens't have this book");
//            }
//        } catch (NoResultException ex) {
//            System.err.println("Book not found: " + ex);
//        } catch (NoSuchElementException ex) {
//            System.err.println("User not found: " + ex);
//        }
//    }

//    @Override
//    public List<? extends Feedback> getUserFeedbacks(String libCard) {
//        Optional<UserDO> user = template.txExpr(em -> getUserByLibCard(libCard, em));
//        if (user.isPresent()) {
//            return user.get().getFeedbacks();
//        }
//        return null;
//    }

    
    // TODO: Check if user's feedback already exists
    @Override
    public void addFeedback(String libCard, Feedback feedback) {
        try {
            BookDO book = template.txExpr(em ->
                    em.createNamedQuery(BookDO.GET_BOOK_BY_TITLE, BookDO.class)
                            .setParameter("title", feedback.getBook().getTitle())
                            .getSingleResult());
            
            UserDO user = template.txExpr(em -> getUserByLibCard(libCard, em)).get();
            if (user.getBooks().contains(book)) {
                FeedbackDO fb = new FeedbackDO();
                fb.setMessage(feedback.getMessage());
                book.addFeedback(fb);
                user.addFeedback(fb);
                template.tx(em -> em.merge(user));
            } else {
                System.err.println("User doesn't have this book");
            }
        } catch (NoResultException ex) {
            System.err.println("Book not found: " + ex);
        } catch (NoSuchElementException ex) {
            System.err.println("User not found: " + ex);
        }
    }

    @Override
    public void removeFeedback(String libCard, String title) {
         try {
            BookDO book = template.txExpr(em ->
                    em.createNamedQuery(BookDO.GET_BOOK_BY_TITLE, BookDO.class)
                            .setParameter("title", title)
                            .getSingleResult());
            
            UserDO user = template.txExpr(em -> getUserByLibCard(libCard, em)).get();
            Iterator<FeedbackDO> it = user.getFeedbacks().iterator();
            while (it.hasNext()) {
                FeedbackDO f = it.next();
                if (f.getBook().equals(book)) {
                    book.removeFeedback(f);
                    it.remove();
                    template.tx(em -> em.merge(book));
                    template.tx(em -> em.merge(user));
                    break;
                }
            }
        } catch (NoResultException ex) {
            System.err.println("Book not found: " + ex);
        } catch (NoSuchElementException ex) {
            System.err.println("User not found: " + ex);
        }
    }    

    private Optional<UserDO> getUserByLibCard(String libCard, EntityManager em) {
        try {
            return Optional.of(em.createNamedQuery(UserDO.GET_USER_BY_LIB_CARD,
                    UserDO.class).setParameter("libCard", libCard)
                    .getSingleResult());
        } catch (NoResultException e) {
            System.err.println("Exception occurred while retrieving user"
                    + "from db: " + e);
            return Optional.empty();
        }
    }
}
