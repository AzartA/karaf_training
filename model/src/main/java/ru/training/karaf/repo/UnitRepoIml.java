package ru.training.karaf.repo;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.validation.ValidationException;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.RoleDO;
import ru.training.karaf.model.Unit;
import ru.training.karaf.model.UnitDO;

public class UnitRepoIml implements UnitRepo {
    private final JpaTemplate template;
    private final RepoImpl<UnitDO> repo;
    private final Class<UnitDO> stdClass = UnitDO.class;

    public UnitRepoIml(JpaTemplate template) {
        this.template = template;
        repo = new RepoImpl<>(template,stdClass);
    }

    public List<? extends Unit> getAll() {
        return template.txExpr(em -> em.createNamedQuery(UnitDO.GET_ALL, UnitDO.class).getResultList());
    }

    @Override
    public List<? extends Unit> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue) {
        return repo.getAll(sortBy, sortOrder, pg, sz, filterField, filterValue);
    }

    @Override
    public List<? extends Unit> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz
    ) {
        return repo.getAll(by, order, field, cond, value, pg, sz);
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        return repo.getCount(field, cond, value, pg, sz);
    }

    @Override
    public Optional<? extends Unit> create(Unit unit) {
        UnitDO unitToCreate = new UnitDO(unit.getName(),unit.getNotation());
        return template.txExpr(em -> {
            if (!(getByName(unit.getName(), em).isPresent())) {
                em.persist(unitToCreate);
                return Optional.of(unitToCreate);
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<? extends Unit> update(long id, Unit unit) {
        return template.txExpr(em -> {
            List<UnitDO> l = getByIdOrName(id, unit.getName(), em);
            if (l.size() > 1) {
                throw new ValidationException("This name is already exist");
            }
            if (!l.isEmpty()) {
                UnitDO unitToUpdate = l.get(0);
                if (unitToUpdate.getId() == id) {
                    unitToUpdate.setName(unit.getName());
                    unitToUpdate.setNotation(unit.getNotation());
                    em.merge(unitToUpdate);
                    return Optional.of(unitToUpdate);
                }
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<? extends Unit> get(long id) {
        return template.txExpr(TransactionType.Required, em -> getById(id, em));
    }

    @Override
    public Optional<? extends Unit> getByName(String name) {
        return template.txExpr(em -> getByName(name, em));
    }

    @Override
    public Optional<? extends Unit> delete(long id) {
        return template.txExpr(em -> getById(id, em).map(l -> {
            l.getClimateParameters().forEach(p -> p.getUnits().remove(l));
            em.remove(l);
            return l;
        }));
    }

    private Optional<UnitDO> getByName(String name, EntityManager em) {
        try {
            return Optional.of(em.createNamedQuery(UnitDO.GET_BY_NAME, UnitDO.class).setParameter("name", name)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    private List<UnitDO> getByIdOrName(long id, String name, EntityManager em) {
        return em.createNamedQuery(UnitDO.GET_BY_ID_OR_NAME, UnitDO.class)
                .setParameter("id", id).setParameter("name", name)
                .getResultList();
    }

    private Optional<UnitDO> getById(long id, EntityManager em) {
        return Optional.ofNullable(em.find(UnitDO.class, id));
    }
}
