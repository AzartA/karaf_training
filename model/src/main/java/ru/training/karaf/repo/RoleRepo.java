package ru.training.karaf.repo;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.Role;
import ru.training.karaf.model.RoleDO;
import ru.training.karaf.model.UserDO;
import ru.training.karaf.wrapper.QueryParams;

import javax.persistence.Query;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

public class RoleRepo {
    private final JpaTemplate template;
    private final Repo repo;
    private static final Class<RoleDO> CLASS = RoleDO.class;

    public RoleRepo(JpaTemplate template) {
        this.template = template;
        repo = new Repo(template);
    }

    public  void init(){
        template.tx(em -> {
            Query q = em.createNativeQuery("SELECT roledo.id FROM roledo LIMIT 1");
            List<?> list = q.getResultList();
            if(list.isEmpty()) {
                RoleDO admin = new RoleDO("Admin");
                RoleDO user = new RoleDO("User");
                RoleDO operator = new RoleDO("Operator");
                em.persist(admin);
                em.persist(user);
                em.persist(operator);
            }
        });
    }

    public Optional<? extends Role> addUsers(long id, List<Long> userIds) {

        return template.txExpr(TransactionType.Required, em -> {
            Optional<RoleDO> roleToUpdate = repo.getById(id, em, CLASS);
            roleToUpdate.ifPresent(p -> {
                p.addUsers(repo.getEntitySetByIds(userIds, em, UserDO.class));
                em.merge(p);
            });
            return roleToUpdate;
        });
    }


    public Optional<? extends Role> removeUsers(long id, List<Long> userIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<RoleDO> roleToUpdate = repo.getById(id, em, CLASS);
            roleToUpdate.ifPresent(p -> {
                p.getUsers().removeAll(repo.getEntitySetByIds(userIds, em, UserDO.class));
                em.merge(p);
            });
            return roleToUpdate;
        });
    }


    public List<? extends Role> getAll(QueryParams query ) {
        return repo.getAll(query, CLASS);
    }


    public long getCount(QueryParams query) {
        return repo.getCount(query, CLASS);
    }


    public Optional<? extends Role> create(Role role) {
        return template.txExpr(em -> {
            if (!(repo.getByName(role.getName(), em, CLASS).isPresent())) {
                RoleDO roleToCreate = new RoleDO(role.getName());
                em.persist(roleToCreate);
                roleToCreate.setUsers(repo.getEntitySet(role.getUsers(), em, UserDO.class));
                return Optional.of(roleToCreate);
            }
            return Optional.empty();
        });
    }


    public Optional<? extends Role> update(long id, Role role) {
        return template.txExpr(em -> {
            List<RoleDO> l = repo.getByIdOrName(id, role.getName(), em, CLASS);
            if (l.size() > 1) {
                throw new ValidationException("This name is already exist");
            }
            if (!l.isEmpty()) {
                RoleDO roleToUpdate = l.get(0);
                if (roleToUpdate.getId() == id) {
                    roleToUpdate.setName(role.getName());
                    roleToUpdate.setUsers(repo.getEntitySet(role.getUsers(), em, UserDO.class));
                    em.merge(roleToUpdate);
                    return Optional.of(roleToUpdate);
                }
            }
            return Optional.empty();
        });
    }


    public Optional<? extends Role> get(long id) {
        return template.txExpr(TransactionType.Required, em -> repo.getById(id, em, CLASS));
    }


    public Optional<? extends Role> delete(long id) {


        return template.txExpr(em -> repo.getById(id, em, CLASS).map(l -> {
            l.getUsers().forEach(u -> u.getRoles().remove(l));
            em.remove(l);
            return l;
        }));
    }
}
