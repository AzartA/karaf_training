package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.validation.ValidationException;

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

    //ToDo think about == 1
    @Override
    public Optional<? extends User> update(long id, User user) {
        return template.txExpr(em -> {
            List<UserDO> u = getByIdOrLogin(id, user.getLogin(), em);
            if (u.size() > 1) {
                throw new ValidationException("This login is already exist");
            }
            if (u.size() == 1) {
                UserDO userToUpdate = u.get(0);
                if (userToUpdate.getId() == id) {
                    userToUpdate.setLogin(user.getLogin());
                    userToUpdate.setName(user.getName());
                    userToUpdate.setProperties(user.getProperties());
                    em.merge(userToUpdate);
                    return Optional.of(userToUpdate);
                }
            }
            return Optional.empty();
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
    public Optional<? extends User> get(long id) {
       // return template.txExpr(em -> getById(id, em));
        return Optional.ofNullable(template.txExpr(em -> em.find(UserDO.class, id)));

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

    private List<UserDO> getByIdOrLogin(long id, String login, EntityManager em) {

        return em.createNamedQuery(UserDO.GET_BY_ID_OR_LOGIN, UserDO.class)
                    .setParameter("id", id).setParameter("login", login)
                    .getResultList();


    }
}
