package ru.training.karaf.repo;

import java.util.ArrayList;
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

    public void init() {
        AvatarDO avatar = new AvatarDO();
        avatar.setPicture("Admin's avatar".getBytes());
        
        UserNameDO name = new UserNameDO();
        name.setFirstName("Admin's first name");
        name.setLastName("Admin's last name");
               
        FeedbackDO feedback = new FeedbackDO();
        feedback.setMessage("Admin's feedback");
        List<FeedbackDO> feedbacks = new ArrayList<>();
        feedbacks.add(feedback);
        
        GenreDO genre = new GenreDO();
        genre.setName("Admin's book genre");

        BookDO book = new BookDO();
        book.setAuthor("Admin's book author");
        book.setGenre(genre);
        book.setTitle("Admin's book title");
        book.setYear(2000);
        book.addFeedback(feedback);
        Set<BookDO> books = new HashSet<>();
        books.add(book);
        
        UserDO admin = new UserDO();
        admin.setAddress("Admin's address");
        admin.setLibCard("Admin's lib card");
        admin.setRegDate(new Date());
        //admin.setAvatar(avatar);
        admin.setUserName(name);
        admin.addFeedback(feedback);
        admin.setBooks(books);
        //admin.setFeedbacks(feedbacks);
        
        template.tx(em -> em.persist(admin));
    }
    
    @Override
    public List<? extends User> getAllUsers() {
        return template.txExpr(em -> em.createNamedQuery
            (UserDO.GET_ALL_USERS, UserDO.class).getResultList());
    }

    @Override
    public void createUser(User user) {
        UserDO userToCreate = new UserDO();
        userToCreate.setAddress(user.getAddress());
        userToCreate.setAvatar((AvatarDO)user.getAvatar());
        userToCreate.setBooks((Set<BookDO>)user.getBooks());
        userToCreate.setFeedbacks((List<FeedbackDO>)user.getFeedbacks());
        userToCreate.setLibCard(user.getLibCard());
        userToCreate.setRegDate(user.getRegDate());
        userToCreate.setUserName((UserNameDO)user.getUserName());

        template.tx(em -> em.persist(userToCreate));
    }

    @Override
    public void updateUser(String libCard, User user) {
        template.tx(em -> {
            getUserByLibCard(libCard, em).ifPresent(userToUpdate -> {
                userToUpdate.setAddress(user.getAddress());
                userToUpdate.setAvatar((AvatarDO)user.getAvatar());
                userToUpdate.setBooks((Set<BookDO>)user.getBooks());
                userToUpdate.setFeedbacks((List<FeedbackDO>)user.getFeedbacks());
                userToUpdate.setLibCard(user.getLibCard());
                userToUpdate.setRegDate(user.getRegDate());
                userToUpdate.setUserName((UserNameDO)user.getUserName());
                
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
