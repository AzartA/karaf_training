package ru.training.karaf.repo;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.validation.ValidationException;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.UnitDO;

public class ClimateParameterRepoImpl implements ClimateParameterRepo {
    private final JpaTemplate template;
    private final RepoImpl<UnitDO> unitRepo;
    private final RepoImpl<ClimateParameterDO> repoImpl;
    private final Class<ClimateParameterDO> cpdClass = ClimateParameterDO.class;

    public ClimateParameterRepoImpl(JpaTemplate template) {
        this.template = template;
        //ToDo fabric?
        unitRepo = new RepoImpl<>(template);
        repoImpl = new RepoImpl<>(template);
    }

    @Override
    public List<? extends ClimateParameter> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue) {
        return repoImpl.getAll(sortBy, sortOrder, pg, sz, filterField, filterValue, cpdClass);
    }

    @Override
    public Optional<? extends ClimateParameter> create(ClimateParameter parameter) {
        ClimateParameterDO paramToCreate = new ClimateParameterDO(parameter.getName());
        return template.txExpr(em -> {
            if (!(repoImpl.getByName(parameter.getName(), em, cpdClass).isPresent())) {
                em.persist(paramToCreate);
                paramToCreate.setUnits(getUnitsById(parameter, em));
                return Optional.of(paramToCreate);
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<? extends ClimateParameter> update(long id, ClimateParameter parameter) {
        return template.txExpr(em -> {
            List<ClimateParameterDO> l = repoImpl.getByIdOrName(id, parameter.getName(), em, cpdClass);
            if (l.size() > 1) {
                throw new ValidationException("This name is already exist");
            }
            if (!l.isEmpty()) {
                ClimateParameterDO parameterToUpdate = l.get(0);
                if (parameterToUpdate.getId() == id) {
                    parameterToUpdate.setName(parameter.getName());
                    parameterToUpdate.setUnits(getUnitsById(parameter, em));
                    em.merge(parameterToUpdate);
                    return Optional.of(parameterToUpdate);
                }
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<? extends ClimateParameter> get(long id) {
        return template.txExpr(TransactionType.Required, em -> repoImpl.getById(id, em, cpdClass));
    }

    @Override
    public Optional<? extends ClimateParameter> getByName(String name) {
        return template.txExpr(em -> repoImpl.getByName(name, em, cpdClass));
    }

    @Override
    public Optional<? extends ClimateParameter> delete(long id) {
        return template.txExpr(em -> repoImpl.getById(id, em, cpdClass).map(l -> {
            l.getUnits().forEach(u -> u.getClimateParameters().remove(l));
            em.remove(l);
            return l;
        }));
    }

    @Override
    public Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<ClimateParameterDO> parameterToUpdate = repoImpl.getById(id, em, cpdClass);
            parameterToUpdate.ifPresent(p -> {
                p.addUnits(unitRepo.getEntitySet(unitIds, em, UnitDO.class));
                em.merge(p);
            });
            return parameterToUpdate;
        });
    }

    private Set<UnitDO> getUnitsById(ClimateParameter parameter, EntityManager em) {
        Set<UnitDO> units = new HashSet<>(4);
        if (parameter.getUnits() != null) {
            List<Long> unitIds = parameter.getUnits().stream().map(Entity::getId).collect(Collectors.toList());
            units = unitRepo.getEntitySet(unitIds, em, UnitDO.class);
        }
        return units;
    }
}
