package ru.training.karaf.repo;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ValidationException;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.UnitDO;

public class ClimateParameterRepoImpl implements ClimateParameterRepo {
    private final JpaTemplate template;
    private final UnitRepoIml unitRepo;
    private final RepoImpl<ClimateParameterDO> repoImpl;

    public ClimateParameterRepoImpl(JpaTemplate template) {

        this.template = template;
        unitRepo = new UnitRepoIml(template);
        repoImpl = new RepoImpl<>(template);
    }

    @Override
    public List<? extends ClimateParameter> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue) {
        return repoImpl.getAll(sortBy, sortOrder, pg, sz, filterField, filterValue, ClimateParameterDO.class);
    }

    @Override
    public Optional<? extends ClimateParameter> create(ClimateParameter parameter) {
        ClimateParameterDO paramToCreate = new ClimateParameterDO(parameter.getName());
        return template.txExpr(em -> {
            if (!(getByName(parameter.getName(), em).isPresent())) {
                em.persist(paramToCreate);
                paramToCreate.setUnits(getUnitsById(parameter));
                return Optional.of(paramToCreate);
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<? extends ClimateParameter> update(long id, ClimateParameter parameter) {
        return template.txExpr(em -> {
            List<ClimateParameterDO> l = getByIdOrName(id, parameter.getName(), em);
            if (l.size() > 1) {
                throw new ValidationException("This name is already exist");
            }
            if (!l.isEmpty()) {
                ClimateParameterDO parameterToUpdate = l.get(0);
                if (parameterToUpdate.getId() == id) {
                    parameterToUpdate.setName(parameter.getName());
                    parameterToUpdate.setUnits(getUnitsById(parameter));
                    em.merge(parameterToUpdate);
                    return Optional.of(parameterToUpdate);
                }
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<? extends ClimateParameter> get(long id) {
        return template.txExpr(TransactionType.Required, em -> getById(id, em));
    }

    @Override
    public Optional<? extends ClimateParameter> getByName(String name) {
        return template.txExpr(em -> getByName(name, em));
    }

    @Override
    public Optional<? extends ClimateParameter> delete(long id) {
        return template.txExpr(em -> getById(id, em).map(l -> {
            l.getUnits().forEach(u -> u.getClimateParameters().remove(l));
            em.remove(l);
            return l;
        }));
    }

 /*   @Override
    public Optional<? extends ClimateParameter> setUnits(long id, List<Long> unitIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<ClimateParameterDO> parameterToUpdate = getById(id, em);
            parameterToUpdate.ifPresent(p -> {
                p.setUnits(getUnitsById(unitIds));
                em.merge(p);
            });
            return parameterToUpdate;
        });
    }*/

    @Override
    public Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds) {
        Set<UnitDO> units = new HashSet<>(4);
        return template.txExpr(TransactionType.Required, em -> {
            Optional<ClimateParameterDO> parameterToUpdate = getById(id, em);
            parameterToUpdate.ifPresent(p -> {
                p.addUnits(getUnitsById(unitIds));
                em.merge(p);
            });
            return parameterToUpdate;
        });
    }

    private Optional<ClimateParameterDO> getByName(String name, EntityManager em) {
        try {
            return Optional.of(em.createNamedQuery(ClimateParameterDO.GET_BY_NAME, ClimateParameterDO.class).setParameter("name", name)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    private List<ClimateParameterDO> getByIdOrName(long id, String name, EntityManager em) {
        return em.createNamedQuery(ClimateParameterDO.GET_BY_ID_OR_NAME, ClimateParameterDO.class)
                .setParameter("id", id).setParameter("name", name)
                .getResultList();
    }

    private Optional<ClimateParameterDO> getById(long id, EntityManager em) {
        return Optional.ofNullable(em.find(ClimateParameterDO.class, id));
    }

    private Set<UnitDO> getUnitsById(List<Long> unitIds) {
        Set<UnitDO> units = new HashSet<>(4);
        unitIds.forEach(uId -> unitRepo.get(uId).ifPresent(un -> units.add((UnitDO) un)));
        return units;
    }

    private Set<UnitDO> getUnitsById(ClimateParameter parameter) {
        Set<UnitDO> units = new HashSet<>(4);
        if (parameter.getUnits() != null) {
            List<Long> unitIds = parameter.getUnits().stream().map(Entity::getId).collect(Collectors.toList());
            unitIds.forEach(uId -> unitRepo.get(uId).ifPresent(un -> units.add((UnitDO) un)));
        }
        return units;
    }
}
