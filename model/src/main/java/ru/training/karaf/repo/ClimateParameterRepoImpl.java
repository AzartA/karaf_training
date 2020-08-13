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
import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.SensorTypeDO;
import ru.training.karaf.model.UnitDO;

public class ClimateParameterRepoImpl implements ClimateParameterRepo {
    private final JpaTemplate template;
    private final RepoImpl<ClimateParameterDO> repoImpl;
    private final Class<ClimateParameterDO> cpdClass = ClimateParameterDO.class;

    public ClimateParameterRepoImpl(JpaTemplate template) {
        this.template = template;
        repoImpl = new RepoImpl<>(template, cpdClass);
    }

    @Override
    public List<? extends ClimateParameter> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue) {
        return repoImpl.getAll(sortBy, sortOrder, pg, sz, filterField, filterValue);
    }

    @Override
    public List<? extends ClimateParameter> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz
    ) {
        return null;
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        return repoImpl.getCount(field, cond, value, pg, sz);
    }

    @Override
    public Optional<? extends ClimateParameter> create(ClimateParameter parameter) {
        ClimateParameterDO paramToCreate = new ClimateParameterDO(parameter.getName());
        return template.txExpr(em -> {
            if (!(repoImpl.getByName(parameter.getName(), em).isPresent())) {
                em.persist(paramToCreate);
                paramToCreate.setUnits(repoImpl.getEntitySet(parameter.getUnits(), em, UnitDO.class));
                paramToCreate.setSensorTypes(repoImpl.getEntitySet(parameter.getSensorTypes(),em, SensorTypeDO.class));
                return Optional.of(paramToCreate);
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<? extends ClimateParameter> update(long id, ClimateParameter parameter) {
        return template.txExpr(em -> {
            List<ClimateParameterDO> l = repoImpl.getByIdOrName(id, parameter.getName(), em);
            if (l.size() > 1) {
                throw new ValidationException("This name is already exist");
            }
            if (!l.isEmpty()) {
                ClimateParameterDO parameterToUpdate = l.get(0);
                if (parameterToUpdate.getId() == id) {
                    parameterToUpdate.setName(parameter.getName());
                    parameterToUpdate.setUnits(repoImpl.getEntitySet(parameter.getUnits(), em, UnitDO.class));
                    parameterToUpdate.setSensorTypes(repoImpl.getEntitySet(parameter.getSensorTypes(),em, SensorTypeDO.class));
                    em.merge(parameterToUpdate);
                    return Optional.of(parameterToUpdate);
                }
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<? extends ClimateParameter> get(long id) {
        return template.txExpr(TransactionType.Required, em -> repoImpl.getById(id, em));
    }

    @Override
    public Optional<? extends ClimateParameter> getByName(String name) {
        return template.txExpr(em -> repoImpl.getByName(name, em));
    }

    @Override
    public Optional<? extends ClimateParameter> delete(long id) {
        return template.txExpr(em -> repoImpl.getById(id, em).map(l -> {
            l.getUnits().forEach(u -> u.getClimateParameters().remove(l));
            l.getSensorTypes().forEach(s -> s.getParameters().remove(l));
            //l.getMeasurings().forEach(m -> m.setParameter(null)); //  delete cascade.ALL in ClimateParameterDO for this case
            em.remove(l);
            return l;
        }));
    }

    @Override
    public Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<ClimateParameterDO> parameterToUpdate = repoImpl.getById(id, em);
            parameterToUpdate.ifPresent(p -> {
                p.addUnits(repoImpl.getEntitySet(unitIds, em, UnitDO.class));
                em.merge(p);
            });
            return parameterToUpdate;
        });
    }

   /* @Override
    public Optional<? extends ClimateParameter> addSensorTypes(long id, List<Long> typeIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<ClimateParameterDO> parameterToUpdate = repoImpl.getById(id, em);
            parameterToUpdate.ifPresent(p -> {
                p.addSensorTypes(typeRepo.getEntitySet(typeIds, em, SensorTypeDO.class));
                em.merge(p);
            });
            return parameterToUpdate;
        });
    }*/

}
