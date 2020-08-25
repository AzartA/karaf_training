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
import ru.training.karaf.wrapper.QueryParams;

public class ClimateParameterRepo {
    private final JpaTemplate template;
    private final Repo repo;
    private static final Class<ClimateParameterDO> CLASS = ClimateParameterDO.class;

    public ClimateParameterRepo(JpaTemplate template) {
        this.template = template;
        repo = new Repo(template);
    }


    public List<? extends ClimateParameter> getAll(QueryParams query ) {
        return repo.getAll(query, CLASS);
    }


    public long getCount(QueryParams query) {
        return repo.getCount(query, CLASS);
    }


    public Optional<? extends ClimateParameter> create(ClimateParameter parameter) {
        ClimateParameterDO paramToCreate = new ClimateParameterDO(parameter.getName());
        return template.txExpr(em -> {
            if (!(repo.getByName(parameter.getName(), em, CLASS).isPresent())) {
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
            List<ClimateParameterDO> l = repo.getByIdOrName(id, parameter.getName(), em, CLASS);
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
        return template.txExpr(TransactionType.Required, em -> repo.getById(id, em, CLASS));
    }


    public Optional<? extends ClimateParameter> getByName(String name) {
        return template.txExpr(em -> repo.getByName(name, em, CLASS));
    }


    public Optional<? extends ClimateParameter> delete(long id) {
        return template.txExpr(em -> repo.getById(id, em, CLASS).map(l -> {
            l.getUnits().forEach(u -> u.getClimateParameters().remove(l));
            l.getSensorTypes().forEach(s -> s.getParameters().remove(l));
            //l.getMeasurings().forEach(m -> m.setParameter(null)); //  delete cascade.ALL in ClimateParameterDO for this case
            em.remove(l);
            return l;
        }));
    }


    public Optional<? extends ClimateParameter> addUnits(long id, List<Long> unitIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<ClimateParameterDO> parameterToUpdate = repo.getById(id, em, CLASS);
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
