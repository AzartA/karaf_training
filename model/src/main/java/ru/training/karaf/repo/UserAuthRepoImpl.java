package ru.training.karaf.repo;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.User;
import ru.training.karaf.model.UserDO;

import javax.persistence.NoResultException;
import java.util.Optional;

public class UserAuthRepoImpl implements UserAuthRepo {
    private final JpaTemplate template;
    public UserAuthRepoImpl(JpaTemplate template) {
        this.template = template;
    }
    public Optional<? extends User> getUser(String login) {
        try {
            return Optional.of((template.txExpr(em -> em.createNamedQuery(UserDO.GET_BY_LOGIN, UserDO.class)
                    .setParameter("login", login).getSingleResult())));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
