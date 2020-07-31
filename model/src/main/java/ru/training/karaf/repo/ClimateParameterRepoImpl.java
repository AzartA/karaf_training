package ru.training.karaf.repo;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.model.UnitDO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ValidationException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ClimateParameterRepoImpl implements ClimateParameterRepo {
    private final JpaTemplate template;
    private final UnitRepoIml unitRepo;

    public ClimateParameterRepoImpl(JpaTemplate template) {

        this.template = template;
        unitRepo = new UnitRepoIml(template);
    }

    @Override
    public List<? extends ClimateParameter> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue) {
        return template.txExpr(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Class<ClimateParameterDO> enClass = ClimateParameterDO.class;
            CriteriaQuery<ClimateParameterDO> cr = cb.createQuery(enClass);
            Root<ClimateParameterDO> root = cr.from(enClass);
            cr.select(root);
            List<String> fieldNames = Arrays.asList(enClass.getDeclaredFields()).stream().map(Field::getName).collect(Collectors.toList());
            if(filterField != null && filterValue != null) {
                if (!fieldNames.contains(filterField)) {
                    throw new ValidationException("There is no such field: " + filterField);
                }
                cr.where(cb.equal(root.get(filterField),filterValue));
            }
            if(sortBy != null && sortOrder != null){
                if(!fieldNames.contains(sortBy)){
                    throw new ValidationException("There is no such field: " + sortBy );
                }
                if("asc".equalsIgnoreCase(sortOrder)) {
                    cr.orderBy(cb.asc(root.get(sortBy)));
                }
                if("desc".equalsIgnoreCase(sortOrder)) {
                    cr.orderBy(cb.desc(root.get(sortBy)));
                }
            }

            TypedQuery query = em.createQuery(cr);

            if (pg > 0 && sz > 0) {
                int offset = (pg - 1) * sz;
                query.setFirstResult(offset)
                        .setMaxResults(sz);
            }
            return query.getResultList();
        });
    }

    @Override
    public Optional<? extends ClimateParameter> create(ClimateParameter parameter) {
        ClimateParameter paramToCreate = new ClimateParameterDO(parameter);
        return template.txExpr(em -> {
            if (!(getByName(parameter.getName(), em).isPresent())) {
                em.persist(paramToCreate);
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
                    parameterToUpdate.setUnits((Set<UnitDO>) parameter.getUnits());
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

    @Override
    public Optional<? extends ClimateParameter> setUnits(long id, List<Long> unitIds) {
        Set<UnitDO> units = new HashSet<>(4);
        return template.txExpr(TransactionType.Required, em -> {
            Optional<ClimateParameterDO> parameterToUpdate = getById(id, em);
            parameterToUpdate.ifPresent(p -> {
                unitIds.forEach(uId -> unitRepo.get(uId).ifPresent(un -> units.add((UnitDO) un)));
                p.setUnits(units);
                em.merge(p);
            });
            return parameterToUpdate;
        });
    }

    @Override
    public Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds) {
        Set<UnitDO> units = new HashSet<>(4);
        return template.txExpr(TransactionType.Required, em -> {
            Optional<ClimateParameterDO> parameterToUpdate = getById(id, em);
            parameterToUpdate.ifPresent(p -> {
                unitIds.forEach(uId -> unitRepo.get(uId).ifPresent(un -> units.add((UnitDO) un)));
                p.addUnits(units);
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
}
