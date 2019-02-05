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
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.AvatarDO;
import ru.training.karaf.model.BookDO;
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
                // TODO: fix this issue
                if (userToUpdate.getAvatar() != null) {
                    if (user.getAvatar() != null) {
                        userToUpdate.getAvatar().setPicture(user.getAvatar().getPicture());
                    } /*else {
                        userToUpdate.setAvatar(null);
                    }*/
                }
                userToUpdate.setLibCard(user.getLibCard());
                userToUpdate.setRegDate(user.getRegDate());
                userToUpdate.getUserName().setFirstName(user.getUserName().getFirstName());
                userToUpdate.getUserName().setLastName(user.getUserName().getLastName());
                
                //em.merge(userToUpdate);

//                Set<BookDO> books = new HashSet<>();
//                user.getBooks().forEach(b -> {
//                    books.add(em.createNamedQuery(BookDO.GET_BOOK_BY_TITLE,
//                            BookDO.class)
//                            .setParameter("title", b.getTitle())
//                            .getSingleResult());
//                });
                //List<FeedbackDO> feedbacks = new ArrayList<>();
//                user.getFeedbacks().forEach(f -> {
//                    FeedbackDO fb = new FeedbackDO();
//                    fb.setBook(em.createNamedQuery(BookDO.GET_BOOK_BY_TITLE,
//                            BookDO.class)
//                            .setParameter("title", f.getBook().getTitle())
//                            .getSingleResult());
//                    fb.setMessage(f.getMessage());
//                    fb.setUser(userToUpdate);
//                    em.merge(fb);
//                    feedbacks.add(fb);
//                });
//                List<String> existingFeedbacks = userToUpdate.getFeedbacks()
//                        .stream()
//                        .map(f -> f.getBook().getTitle())
//                        .collect(Collectors.toList());
//                
//                List<String> changedFeedbacks = user.getFeedbacks()
//                        .stream()
//                        .map(f -> f.getBook().getTitle())
//                        .filter(t -> !existingFeedbacks.contains(t))
//                        .collect(Collectors.toList());
//                
//                user.getFeedbacks().forEach(f -> {
//                    if (changedFeedbacks.contains(f.getBook().getTitle())) {
//                        FeedbackDO fb = new FeedbackDO();
//                        fb.setMessage(f.getMessage());
//                        fb.setBook(em.createNamedQuery(BookDO.GET_BOOK_BY_TITLE,
//                            BookDO.class)
//                            .setParameter("title", f.getBook().getTitle())
//                            .getSingleResult());
//                        userToUpdate.addFeedback(fb);
//                    }
//                });
                //userToUpdate.setFeedbacks(feedbacks);
                //userToUpdate.setBooks(books);
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

    
//    // TODO: Check if user's feedback already exists
//    @Override
//    public void addFeedback(String libCard, Feedback feedback) {
//        try {
//            BookDO book = template.txExpr(em ->
//                    em.createNamedQuery(BookDO.GET_BOOK_BY_TITLE, BookDO.class)
//                            .setParameter("title", feedback.getBook().getTitle())
//                            .getSingleResult());
//            
//            UserDO user = template.txExpr(em -> getUserByLibCard(libCard, em)).get();
//            if (user.getBooks().contains(book)) {
//                FeedbackDO fb = new FeedbackDO();
//                fb.setMessage(feedback.getMessage());
//                book.addFeedback(fb);
//                user.addFeedback(fb);
//                template.tx(em -> em.merge(user));
//            } else {
//                System.err.println("User doesn't have this book");
//            }
//        } catch (NoResultException ex) {
//            System.err.println("Book not found: " + ex);
//        } catch (NoSuchElementException ex) {
//            System.err.println("User not found: " + ex);
//        }
//    }

//    @Override
//    public void removeFeedback(String libCard, String title) {
//         try {
//            BookDO book = template.txExpr(em ->
//                    em.createNamedQuery(BookDO.GET_BOOK_BY_TITLE, BookDO.class)
//                            .setParameter("title", title)
//                            .getSingleResult());
//            
//            UserDO user = template.txExpr(em -> getUserByLibCard(libCard, em)).get();
//            Iterator<FeedbackDO> it = user.getFeedbacks().iterator();
//            while (it.hasNext()) {
//                FeedbackDO f = it.next();
//                if (f.getBook().equals(book)) {
//                    book.removeFeedback(f);
//                    it.remove();
//                    template.tx(em -> em.merge(book));
//                    template.tx(em -> em.merge(user));
//                    break;
//                }
//            }
//        } catch (NoResultException ex) {
//            System.err.println("Book not found: " + ex);
//        } catch (NoSuchElementException ex) {
//            System.err.println("User not found: " + ex);
//        }
    
    
    @Override
    public void addBook(String libCard, String title) {
        try {
            UserDO user = template.txExpr(em -> em
                    .createNamedQuery(UserDO.GET_USER_BY_LIB_CARD, UserDO.class)
                    .setParameter("libCard", libCard)
                    .getSingleResult());
            BookDO book = template.txExpr(em -> em
                    .createNamedQuery(BookDO.GET_BOOK_BY_TITLE, BookDO.class)
                    .setParameter("title", title)
                    .getSingleResult());
            
            template.tx(em -> em
                    .createNamedQuery(UserDO.ADD_BOOK)
                    .setParameter(1, user.getId())
                    .setParameter(2, book.getId())
                    .executeUpdate());

            // TODO: check if user already has this book
//            template.tx(em -> em
//                    .createNativeQuery(UserDO.ADD_BOOK)
//                    .setParameter(1, user.getId())
//                    .setParameter(2, book.getId())
//                    .executeUpdate());
            
        } catch (NoResultException e) {
            System.err.println("User/book not found");
        }

    }

    private Optional<UserDO> getUserByLibCard(String libCard, EntityManager em) {
        try {
            return Optional.of(em.createNamedQuery(UserDO.GET_USER_BY_LIB_CARD,
                    UserDO.class).setParameter("libCard", libCard)
                    .getSingleResult());
        } catch (NoResultException e) {
            System.err.println("User not found: " + e);
            return Optional.empty();
        }
    }
}
