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
        InputStream in = new FileInputStream("C:\\Projects\\karaf_training\\a.png");
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

        template.tx(em -> {
            em.persist(genre);
            em.persist(admin);
        });
    }
    
    @Override
    public List<? extends User> getAllUsers() {
        return template.txExpr(em -> em.createNamedQuery
            (UserDO.GET_ALL_USERS, UserDO.class).getResultList());
    }
    
    @Override
    public void createUser(User user) {
        template.tx(em -> em.persist(user));
    }

    @Override
    public void updateUser(User user) {
        template.tx(em -> em.merge(user));
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
            System.err.println("User not found: " + e);
            return Optional.empty();
        }
    }
}
