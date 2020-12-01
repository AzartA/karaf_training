package ru.training.karaf.repo;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.SensorType;
import ru.training.karaf.model.SensorTypeDO;
import ru.training.karaf.wrapper.QueryParams;

import javax.persistence.EntityManager;
import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SensorTypeRepo {
    private final JpaTemplate template;
    private final Repo repo;
    private static final Class<SensorTypeDO> CLASS = SensorTypeDO.class;

    public SensorTypeRepo(JpaTemplate template) {
        this.template = template;
        repo = new Repo(template);
    }

    public Optional<? extends SensorType> addParams(long id, List<Long> paramIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<SensorTypeDO> typeToUpdate = repo.getById(id, em, CLASS);
            typeToUpdate.ifPresent(sensorTypeDO -> {
                sensorTypeDO.addParameters(repo.getEntitySetByIds(paramIds, em, ClimateParameterDO.class));
                em.merge(sensorTypeDO);
            });
            return typeToUpdate;
        });
    }

    public List<? extends SensorType> getAll(QueryParams query) {
        return repo.getAll(query, CLASS);
    }

    public long getCount(QueryParams query) {
        return repo.getCount(query, CLASS);
    }

    public Optional<? extends SensorType> create(SensorType type) {
        return template.txExpr(em -> {
            if (!(repo.getByName(type.getName(), em, CLASS).isPresent())) {
                SensorTypeDO typeToCreate = new SensorTypeDO(type);
                em.persist(typeToCreate);
                typeToCreate.setParameters(getParamsById(type, em));
                return Optional.of(typeToCreate);
            }
            return Optional.empty();
        });
    }

    public Optional<? extends SensorType> update(long id, SensorType type) {
        return template.txExpr(em -> {
            List<SensorTypeDO> sensorTypes = repo.getByIdOrName(id, type.getName(), em, CLASS);
            if (sensorTypes.size() > 1) {
                throw new ValidationException("This name is already exist");
            }
            if (!sensorTypes.isEmpty()) {
                SensorTypeDO typeToUpdate = sensorTypes.get(0);
                if (typeToUpdate.getId() == id) {
                    typeToUpdate.update(type);
                    typeToUpdate.setParameters(getParamsById(type, em));
                    em.merge(typeToUpdate);
                    return Optional.of(typeToUpdate);
                }
            }
            return Optional.empty();
        });
    }

    public Optional<? extends SensorType> get(long id) {
        return template.txExpr(TransactionType.Required, em -> repo.getById(id, em, CLASS));
    }

    public Optional<? extends SensorType> delete(long id) {
        return template.txExpr(em -> repo.getById(id, em, CLASS).map(sensorTypeDO -> {
                    sensorTypeDO.getParameters().forEach(u -> u.getSensorTypes().remove(sensorTypeDO));
                    em.remove(sensorTypeDO);
                    return sensorTypeDO;
                })

        );
    }

    private Set<ClimateParameterDO> getParamsById(SensorType type, EntityManager em) {
        Set<ClimateParameterDO> params = new HashSet<>(4);
        if (type.getParameters() != null) {
            List<Long> paramsIds = type.getParameters().stream().map(Entity::getId).collect(Collectors.toList());
            params = repo.getEntitySetByIds(paramsIds, em, ClimateParameterDO.class);
        }
        return params;
    }
}
