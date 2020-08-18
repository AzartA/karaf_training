package ru.training.karaf.repo;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.User;
import ru.training.karaf.model.UserDO;

import javax.persistence.NoResultException;
import java.util.Optional;

public class UserAuthRepo {
    private final JpaTemplate template;
    public UserAuthRepo(JpaTemplate template) {
        this.template = template;
    }
    public User getUser(String login) {
        return (template.txExpr(em -> em.createNamedQuery(UserDO.GET_BY_LOGIN, UserDO.class)
                    .setParameter("login", login).getSingleResult()));
    }
}
