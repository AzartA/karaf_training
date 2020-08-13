package ru.training.karaf.repo;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.Role;
import ru.training.karaf.model.RoleDO;
import ru.training.karaf.model.UserDO;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

public class RoleRepoImpl implements RoleRepo {
    private final JpaTemplate template;
    private final RepoImpl<RoleDO> repo;
    private final Class<RoleDO> stdClass = RoleDO.class;

    public RoleRepoImpl(JpaTemplate template) {
        this.template = template;
        repo = new RepoImpl<>(template, stdClass);
    }

    @Override
    public Optional<? extends Role> addUsers(long id, List<Long> userIds) {

        return template.txExpr(TransactionType.Required, em -> {
            Optional<RoleDO> roleToUpdate = repo.getById(id, em);
            roleToUpdate.ifPresent(p -> {
                p.addUsers(repo.getEntitySet(userIds, em, UserDO.class));
                em.merge(p);
            });
            return roleToUpdate;
        });
    }

    @Override
    public Optional<? extends Role> removeUsers(long id, List<Long> userIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<RoleDO> roleToUpdate = repo.getById(id, em);
            roleToUpdate.ifPresent(p -> {
                p.getUsers().removeAll(repo.getEntitySet(userIds, em, UserDO.class));
                em.merge(p);
            });
            return roleToUpdate;
        });
    }

    @Override
    public List<? extends Role> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue) {
        return null;
    }

    @Override
    public List<? extends Role> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz
    ) {
        return repo.getAll(by, order, field, cond, value, pg, sz);
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        return repo.getCount(field, cond, value, pg, sz);
    }

    @Override
    public Optional<? extends Role> create(Role role) {
        return template.txExpr(em -> {
            if (!(repo.getByName(role.getName(), em).isPresent())) {
                RoleDO roleToCreate = new RoleDO(role.getName());
                em.persist(roleToCreate);
                roleToCreate.setUsers(repo.getEntitySet(role.getUsers(), em, UserDO.class));
                return Optional.of(roleToCreate);
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<? extends Role> update(long id, Role role) {
        return template.txExpr(em -> {
            List<RoleDO> l = repo.getByIdOrName(id, role.getName(), em);
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

    @Override
    public Optional<? extends Role> get(long id) {
        return template.txExpr(TransactionType.Required, em -> repo.getById(id, em));
    }

    @Override
    public Optional<? extends Role> delete(long id) {


        return template.txExpr(em -> repo.getById(id, em).map(l -> {
            l.getUsers().forEach(u -> u.getRoles().remove(l));
            em.remove(l);
            return l;
        }));
    }
}
