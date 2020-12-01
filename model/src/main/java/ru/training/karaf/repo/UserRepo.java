package ru.training.karaf.repo;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.RoleDO;
import ru.training.karaf.model.SensorDO;
import ru.training.karaf.model.User;
import ru.training.karaf.model.UserDO;
import ru.training.karaf.wrapper.QueryParams;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

public class UserRepo {
    private final JpaTemplate template;
    private final Repo repo;
    private final Class<UserDO> CLASS = UserDO.class;

    public UserRepo(JpaTemplate template) {
        this.template = template;
        repo = new Repo(template);
    }

    public List<? extends User> getAll(QueryParams query) {
        return repo.getAll(query, CLASS);
    }

    public long getCount(QueryParams query) {
        return repo.getCount(query, CLASS);
    }

    public User create(User user) {
        return template.txExpr(em -> {
            UserDO userToCreate = new UserDO(user);
            em.persist(userToCreate);
            userToCreate.setSensors(repo.getEntitySet(user.getSensors(), em, SensorDO.class));
            userToCreate.setRoles(repo.getEntitySet(user.getRoles(), em, RoleDO.class));
            return userToCreate;
        });
    }

    public Optional<? extends User> update(long id, User user) {
        return template.txExpr(em -> {
            List<UserDO> users = getByIdOrLogin(id, user.getLogin(), em);
            if (users.size() > 1) {
                throw new ValidationException("This login is already exist");
            }
            if (!users.isEmpty()) {
                UserDO userToUpdate = users.get(0);
                if (userToUpdate.getId() == id) {
                    if (user.getLogin() != null) {
                        userToUpdate.setLogin(user.getLogin());
                    }
                    if (user.getName() != null) {
                        userToUpdate.setName(user.getName());
                    }
                    userToUpdate.setPassword(user.getPassword());
                    if (user.getProperties() != null) {
                        userToUpdate.setProperties(user.getProperties());
                    }
                    userToUpdate.setSensors(repo.getEntitySet(user.getSensors(), em, SensorDO.class));
                    userToUpdate.setRoles(repo.getEntitySet(user.getRoles(), em, RoleDO.class));
                    em.merge(userToUpdate);
                    return Optional.of(userToUpdate);
                }
            }
            return Optional.empty();
        });
    }

    private List<UserDO> getByIdOrLogin(long id, String login, EntityManager em) {
        return em.createNamedQuery(UserDO.GET_BY_ID_OR_LOGIN, UserDO.class)
                .setParameter("id", id).setParameter("login", login)
                .getResultList();
    }

    public Optional<? extends User> get(long id) {
        return Optional.ofNullable(template.txExpr(em -> em.find(UserDO.class, id)));
    }

    public Optional<? extends User> delete(long id) {
        return template.txExpr(em -> repo.getById(id, em, CLASS).map(user -> {
            user.getSensors().forEach(sensor -> sensor.getUsers().remove(user));
            em.remove(user);
            return user;
        }));
    }

    public Optional<? extends User> addSensors(long id, List<Long> sensorIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<UserDO> userToUpdate = repo.getById(id, em, CLASS);
            userToUpdate.ifPresent(userDO -> {
                userDO.addSensors(repo.getEntitySetByIds(sensorIds, em, SensorDO.class));
                em.merge(userDO);
            });
            return userToUpdate;
        });
    }

    public Optional<? extends User> getByLogin(String login) {
        return template.txExpr(em -> getByLogin(login, em));
    }

    public Optional<? extends User> addRoles(long id, List<Long> rolesIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<UserDO> userToUpdate = repo.getById(id, em, CLASS);
            userToUpdate.ifPresent(p -> {
                p.addRoles(repo.getEntitySetByIds(rolesIds, em, RoleDO.class));
                em.merge(p);
            });
            return userToUpdate;
        });
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
