package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.User;
import ru.training.karaf.model.UserDO;

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
    public Optional<? extends User> update(long id, User user) {
        return template.txExpr(em -> {
            Optional<UserDO> u = getByIdOrLogin(id, user.getLogin(), em);
            if (u == null) {
                return null;
            }
            u = u.map(userToUpdate -> {
                userToUpdate.setLogin(user.getLogin());
                userToUpdate.setName(user.getName());
                userToUpdate.setProperties(user.getProperties());
                em.merge(userToUpdate);
                return userToUpdate;
            });
            return u;
        });
    }

    @Override
    public Optional<? extends User> updateById(long id, User user) {
        return template.txExpr(em -> getById(id, em).map(userToUpdate -> {
            userToUpdate.setLogin(user.getLogin());
            userToUpdate.setName(user.getName());
            userToUpdate.setProperties(user.getProperties());
            em.merge(userToUpdate);
            return userToUpdate;
        }));
    }

    @Override
    public Optional<? extends User> updateByLogin(String login, User user) {
        return template.txExpr(em -> getByLogin(login, em).map(userToUpdate -> {
            userToUpdate.setLogin(user.getLogin());
            userToUpdate.setName(user.getName());
            userToUpdate.setProperties(user.getProperties());
            em.merge(userToUpdate);
            return userToUpdate;
        }));
    }

    @Override
    public Optional<? extends User> get(long id) {
        return template.txExpr(em -> getById(id, em));
    }

    @Override
    public Optional<? extends User> getByLogin(String login) {
        return template.txExpr(em -> getByLogin(login, em));
    }

    @Override
    public Optional<UserDO> delete(long id) {
        return template.txExpr(em -> getById(id, em).map(user -> {
            em.remove(user);
            return user;
        }));
    }

    @Override
    public boolean loginIsPresent(String login) {
        return template.txExpr(em -> getByLogin(login, em).isPresent());
    }

    private Optional<UserDO> getByLogin(String login, EntityManager em) {
        try {
            return Optional.of(em.createNamedQuery(UserDO.GET_BY_LOGIN, UserDO.class).setParameter("login", login)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    private Optional<UserDO> getById(long id, EntityManager em) {
        try {
            return Optional.of(em.createNamedQuery(UserDO.GET_BY_ID, UserDO.class).setParameter("id", id)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    private Optional<UserDO> getByIdOrLogin(long id, String login, EntityManager em) {

        try {
            List<UserDO> list = em.createNamedQuery(UserDO.GET_BY_ID_OR_LOGIN, UserDO.class)
                    .setParameter("id", id).setParameter("login", login)
                    .getResultList();
            if (list.size() == 1) {
                return Optional.of(list.get(0));
            }
            return null;
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
