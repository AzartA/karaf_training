package ru.training.karaf.repo;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.validation.ValidationException;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.SensorDO;
import ru.training.karaf.model.User;
import ru.training.karaf.model.UserDO;

public class UserRepoImpl implements UserRepo {
    private final JpaTemplate template;
    private final RepoImpl<UserDO> repo;
    private final Class<UserDO> stdClass = UserDO.class;

    public UserRepoImpl(JpaTemplate template) {
        this.template = template;
        repo = new RepoImpl<>(template, stdClass);
    }

    @Override
    public List<? extends User> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue) {
        return null;
    }

    @Override
    public List<? extends User> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        return repo.getAll(by, order, field, cond, value, pg, sz);
    }

    @Override
    public Optional<? extends User> create(User user) {
        return template.txExpr(em -> {
            UserDO userToCreate = new UserDO(user);
            em.persist(userToCreate);
            return Optional.of(userToCreate);
        });
    }

    @Override
    public Optional<? extends User> update(long id, User user) {
        return template.txExpr(em -> {
            List<UserDO> u = repo.getByIdOrName(id, user.getLogin(), em);
            if (u.size() > 1) {
                throw new ValidationException("This login is already exist");
            }
            if (!u.isEmpty()) {
                UserDO userToUpdate = u.get(0);
                if (userToUpdate.getId() == id) {
                    userToUpdate.setLogin(user.getLogin());
                    userToUpdate.setName(user.getName());
                    userToUpdate.setProperties(user.getProperties());
                    userToUpdate.setSensors(repo.getEntitySet(user.getSensors(),em,SensorDO.class));
                    em.merge(userToUpdate);
                    return Optional.of(userToUpdate);
                }
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<? extends User> get(long id) {
        return Optional.ofNullable(template.txExpr(em -> em.find(UserDO.class, id)));

    }

    @Override
    public Optional<? extends User> delete(long id) {
        return template.txExpr(em -> repo.getById(id,em).map(user -> {
            user.getSensors().forEach(s -> s.getUsers().remove(user));
            em.remove(user);
            return user;
        }));
    }

    @Override
    public Optional<? extends User> addSensors(long id, List<Long> sensorIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<UserDO> userToUpdate = repo.getById(id, em);
            userToUpdate.ifPresent(u -> {
                u.addSensors(repo.getEntitySet(sensorIds, em, SensorDO.class));
                em.merge(u);
            });
            return userToUpdate;
        });
    }

    @Override
    public boolean loginIsPresent(String login) {
        return template.txExpr(em -> getByLogin(login, em).isPresent());
    }

    @Override
    public Optional<? extends User> getByLogin(String login) {
        return template.txExpr(em -> getByLogin(login, em));
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
