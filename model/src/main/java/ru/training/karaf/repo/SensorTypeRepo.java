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
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.SensorType;
import ru.training.karaf.model.SensorTypeDO;

public class SensorTypeRepo {
    private final JpaTemplate template;
    private final Repo typeRepo;
    private final Class<SensorTypeDO> stdClass = SensorTypeDO.class;

    public SensorTypeRepo(JpaTemplate template) {
        this.template = template;
        typeRepo = new Repo(template);
    }


    public Optional<? extends SensorType> addParams(long id, List<Long> paramIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<SensorTypeDO> typeToUpdate = typeRepo.getById(id, em, stdClass);
            typeToUpdate.ifPresent(p -> {
                p.addParameters(typeRepo.getEntitySetByIds(paramIds, em, ClimateParameterDO.class));
                em.merge(p);
            });
            return typeToUpdate;
        });
    }


    public List<? extends SensorType> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz,
            String[] auth) {
        return null;//typeRepo.getAll(by, order, field, cond, value, pg, sz, auth, stdClass);
    }


    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz,
                         String[] auth) {
        return 0;//typeRepo.getCount(field, cond, value, pg, sz, auth, stdClass);
    }


    public Optional<? extends SensorType> create(SensorType type) {
        return template.txExpr(em -> {
            if (!(typeRepo.getByName(type.getName(), em, stdClass).isPresent())) {
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
            List<SensorTypeDO> l = typeRepo.getByIdOrName(id, type.getName(), em, stdClass);
            if (l.size() > 1) {
                throw new ValidationException("This name is already exist");
            }
            if (!l.isEmpty()) {
                SensorTypeDO typeToUpdate = l.get(0);
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
        return template.txExpr(TransactionType.Required, em -> typeRepo.getById(id, em, stdClass));
    }


    public Optional<? extends SensorType> delete(long id) {
        return template.txExpr(em -> typeRepo.getById(id, em, stdClass).map(l -> {
                l.getParameters().forEach(u -> u.getSensorTypes().remove(l));
                //l.getSensors().forEach(s -> s.setType(null)); //  delete cascade.ALL in SensorTypeDO for this case
                em.remove(l);
                return l;
            })

        );
    }

    private Set<ClimateParameterDO> getParamsById(SensorType type, EntityManager em) {
        Set<ClimateParameterDO> params = new HashSet<>(4);
        if (type.getParameters() != null) {
            List<Long> paramsIds = type.getParameters().stream().map(Entity::getId).collect(Collectors.toList());
            params = typeRepo.getEntitySetByIds(paramsIds, em, ClimateParameterDO.class);
        }
        return params;
    }
}
