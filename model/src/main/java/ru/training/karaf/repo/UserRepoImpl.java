package ru.training.karaf.repo;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.User;
import ru.training.karaf.model.UserDO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class UserRepoImpl implements UserRepo {
    private final JpaTemplate template;

    public UserRepoImpl(JpaTemplate template) {
        this.template = template;
    }

    @Override
    public List<? extends User> getAll() {
        return template.txExpr(em -> em.createNamedQuery(UserDO.GET_ALL, UserDO.class).getResultList());
    }

    @Override
    public User create(User user) {
        UserDO userToCreate = new UserDO();
        userToCreate.setLogin(user.getLogin());
        userToCreate.setName(user.getName());
        userToCreate.setProperties(user.getProperties());
        template.tx(em -> em.persist(userToCreate));
        return userToCreate;
    }

    @Override
    public Optional<? extends User> update(String login, User user) {
        return template.txExpr(em -> getByLogin(login, em).map(userToUpdate -> {
            userToUpdate.setLogin(user.getLogin());
            userToUpdate.setName(user.getName());
            userToUpdate.setProperties(user.getProperties());
            em.merge(userToUpdate);
            return userToUpdate;
        }));

    }

    @Override
    public Optional<? extends User> get(String login) {
        return template.txExpr(em -> getByLogin(login, em));
    }

    @Override
    public Optional<UserDO> delete(String login) {
        return template.txExpr(em -> getByLogin(login, em).map(user -> {em.remove(user); return user;}));
    }

    @Override
    public boolean loginIsPresent(String login) {
       return !template.txExpr(em -> getByLogin(login, em).isPresent());
    }

    private Optional<UserDO> getByLogin(String login, EntityManager em) {
        try {
            return Optional.of(em.createNamedQuery(UserDO.GET_BY_LOGIN, UserDO.class).setParameter("login", login)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
