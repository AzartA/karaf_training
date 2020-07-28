package ru.training.karaf.repo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.validation.ValidationException;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.model.Unit;

public class ClimateParameterRepoImpl implements ClimateParameterRepo {
    private JpaTemplate template;
    private UnitRepoIml unitRepo;

    public ClimateParameterRepoImpl(JpaTemplate template) {

        this.template = template;
        unitRepo = new UnitRepoIml(template);
    }

    @Override
    public List<? extends ClimateParameter> getAll() {
        return template.txExpr(em -> em.createNamedQuery(ClimateParameterDO.GET_ALL, ClimateParameterDO.class).getResultList());
    }

    @Override
    public Optional<? extends ClimateParameter> create(ClimateParameter parameter) {
        ClimateParameter paramToCreate = new ClimateParameterDO(parameter.getName(), parameter.getUnits());
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
                    parameterToUpdate.setUnits(parameter.getUnits());
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
            em.remove(l);
            return l;
        }));
    }

    @Override
    public Optional<? extends ClimateParameter> setUnits(long id, List<Long> unitIds) {
        Set<Unit> units = new HashSet<>(4);
        unitIds.stream().forEach(uId -> unitRepo.get(uId).ifPresent(units::add));
        return template.txExpr(TransactionType.Required, em -> {
            Optional<ClimateParameterDO> parameterToUpdate = getById(id, em);
            parameterToUpdate.ifPresent(p -> {
                p.setUnits(units);
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
