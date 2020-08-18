package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import javax.validation.ValidationException;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.model.SensorTypeDO;
import ru.training.karaf.model.UnitDO;

public class ClimateParameterRepo {
    private final JpaTemplate template;
    private final Repo repo;
    private final Class<ClimateParameterDO> stdClass = ClimateParameterDO.class;

    public ClimateParameterRepo(JpaTemplate template) {
        this.template = template;
        repo = new Repo(template);
    }


    public List<? extends ClimateParameter> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz,
            String[] auth
    ) {
        return repo.getAll(by, order, field, cond, value, pg, sz, auth, stdClass);
    }


    public long getCount(
            List<String> field, List<String> cond, List<String> value, int pg, int sz,
            String[] auth
    ) {
        return repo.getCount(field, cond, value, pg, sz, auth, stdClass );
    }


    public Optional<? extends ClimateParameter> create(ClimateParameter parameter) {
        ClimateParameterDO paramToCreate = new ClimateParameterDO(parameter.getName());
        return template.txExpr(em -> {
            if (!(repo.getByName(parameter.getName(), em, stdClass).isPresent())) {
                em.persist(paramToCreate);
                paramToCreate.setUnits(repo.getEntitySet(parameter.getUnits(), em, UnitDO.class));
                paramToCreate.setSensorTypes(repo.getEntitySet(parameter.getSensorTypes(), em, SensorTypeDO.class));
                return Optional.of(paramToCreate);
            }
            return Optional.empty();
        });
    }


    public Optional<? extends ClimateParameter> update(long id, ClimateParameter parameter) {
        return template.txExpr(em -> {
            List<ClimateParameterDO> l = repo.getByIdOrName(id, parameter.getName(), em, stdClass);
            if (l.size() > 1) {
                throw new ValidationException("This name is already exist");
            }
            if (!l.isEmpty()) {
                ClimateParameterDO parameterToUpdate = l.get(0);
                if (parameterToUpdate.getId() == id) {
                    parameterToUpdate.setName(parameter.getName());
                    parameterToUpdate.setUnits(repo.getEntitySet(parameter.getUnits(), em, UnitDO.class));
                    parameterToUpdate.setSensorTypes(repo.getEntitySet(parameter.getSensorTypes(), em, SensorTypeDO.class));
                    em.merge(parameterToUpdate);
                    return Optional.of(parameterToUpdate);
                }
            }
            return Optional.empty();
        });
    }


    public Optional<? extends ClimateParameter> get(long id) {
        return template.txExpr(TransactionType.Required, em -> repo.getById(id, em, stdClass));
    }


    public Optional<? extends ClimateParameter> getByName(String name) {
        return template.txExpr(em -> repo.getByName(name, em, stdClass));
    }


    public Optional<? extends ClimateParameter> delete(long id) {
        return template.txExpr(em -> repo.getById(id, em, stdClass).map(l -> {
            l.getUnits().forEach(u -> u.getClimateParameters().remove(l));
            l.getSensorTypes().forEach(s -> s.getParameters().remove(l));
            //l.getMeasurings().forEach(m -> m.setParameter(null)); //  delete cascade.ALL in ClimateParameterDO for this case
            em.remove(l);
            return l;
        }));
    }


    public Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<ClimateParameterDO> parameterToUpdate = repo.getById(id, em, stdClass);
            parameterToUpdate.ifPresent(p -> {
                p.addUnits(repo.getEntitySetByIds(unitIds, em, UnitDO.class));
                em.merge(p);
            });
            return parameterToUpdate;
        });
    }

   /*
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
