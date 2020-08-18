package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.validation.ValidationException;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.RoleDO;
import ru.training.karaf.model.SensorDO;
import ru.training.karaf.model.User;
import ru.training.karaf.model.UserDO;

public class UserRepo {
    private final JpaTemplate template;


    private final Repo repo;
    private final Class<UserDO> stdClass = UserDO.class;

    public UserRepo(JpaTemplate template) {
        this.template = template;
        repo = new Repo(template);

    }


    public List<? extends User> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz,
            String[] auth) {
        return repo.getAll(by, order, field, cond, value, pg, sz, auth, stdClass);
    }


    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz,
                         String[] auth) {
        return repo.getCount(field, cond, value, pg, sz, auth, stdClass);
    }


    public Optional<? extends User> create(User user) {
        return template.txExpr(em -> {
            UserDO userToCreate = new UserDO(user);
            em.persist(userToCreate);
            userToCreate.setSensors(repo.getEntitySet(user.getSensors(),em,SensorDO.class));
            userToCreate.setRoles(repo.getEntitySet(user.getRoles(),em,RoleDO.class));
            return Optional.of(userToCreate);
        });
    }


    public Optional<? extends User> update(long id, User user) {
        return template.txExpr(em -> {
            List<UserDO> u = repo.getByIdOrName(id, user.getLogin(), em, stdClass);
            if (u.size() > 1) {
                throw new ValidationException("This login is already exist");
            }
            if (!u.isEmpty()) {
                UserDO userToUpdate = u.get(0);
                if (userToUpdate.getId() == id) {
                    if(user.getLogin()!= null) {
                        userToUpdate.setLogin(user.getLogin());
                    }
                    if(user.getName()!= null) {
                        userToUpdate.setName(user.getName());
                    }
                    if(user.getProperties()!= null) {
                        userToUpdate.setProperties(user.getProperties());
                    }
                    userToUpdate.setSensors(repo.getEntitySet(user.getSensors(),em,SensorDO.class));
                    userToUpdate.setRoles(repo.getEntitySet(user.getRoles(),em,RoleDO.class));
                    em.merge(userToUpdate);
                    return Optional.of(userToUpdate);
                }
            }
            return Optional.empty();
        });
    }


    public Optional<? extends User> get(long id) {
        return Optional.ofNullable(template.txExpr(em -> em.find(UserDO.class, id)));

    }


    public Optional<? extends User> delete(long id) {
        return template.txExpr(em -> repo.getById(id,em, stdClass).map(user -> {
            user.getSensors().forEach(s -> s.getUsers().remove(user));
            em.remove(user);
            return user;
        }));
    }


    public Optional<? extends User> addSensors(long id, List<Long> sensorIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<UserDO> userToUpdate = repo.getById(id, em, stdClass);
            userToUpdate.ifPresent(u -> {
                u.addSensors(repo.getEntitySetByIds(sensorIds, em, SensorDO.class));
                em.merge(u);
            });
            return userToUpdate;
        });
    }




    public boolean loginIsPresent(String login) {
        return template.txExpr(em -> getByLogin(login, em).isPresent());
    }


    public Optional<? extends User> getByLogin(String login) {
        return template.txExpr(em -> getByLogin(login, em));
    }


    public Optional<? extends User> addRoles(long id, List<Long> rolesIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<UserDO> userToUpdate = repo.getById(id, em, stdClass);
            userToUpdate.ifPresent(p -> {
                p.addRoles(repo.getEntitySetByIds(rolesIds, em, RoleDO.class));
                em.merge(p);
            });
            return userToUpdate;
        });
    }


    public Optional<? extends User> removeRoles(long id, List<Long> rolesIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<UserDO> userToUpdate = repo.getById(id, em, stdClass);
            userToUpdate.ifPresent(p -> {
                p.removeRoles(repo.getEntitySetByIds(rolesIds, em, RoleDO.class));
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
