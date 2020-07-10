package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.aries.jpa.template.JpaTemplate;

import ru.training.karaf.model.User;
import ru.training.karaf.model.UserDO;

public class UserRepoImpl implements UserRepo {
    private JpaTemplate template;

    public UserRepoImpl(JpaTemplate template) {
        this.template = template;
    }

    @Override
    public List<? extends User> getAll() {
        return template.txExpr(em -> em.createNamedQuery(UserDO.GET_ALL, UserDO.class).getResultList());
    }

    @Override
    public void create(User user) {
        UserDO userToCreate = new UserDO();
        userToCreate.setLogin(user.getLogin());
        userToCreate.setName(user.getName());
        userToCreate.setProperties(user.getProperties());
        template.tx(em -> em.persist(userToCreate));
    }

    @Override
    public void update(String login, User user) {
        template.tx(em -> {
                getByLogin(login, em).ifPresent(userToUpdate -> {
                userToUpdate.setLogin(user.getLogin());
                userToUpdate.setName(user.getName());
                userToUpdate.setProperties(user.getProperties());
                em.merge(userToUpdate);
            });
        });
    }

    @Override
    public Optional<? extends User> get(String login) {
        return template.txExpr(em -> getByLogin(login, em));
    }

    @Override
    public void delete(String login) {
        template.tx(em -> getByLogin(login, em).ifPresent(em::remove));
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
