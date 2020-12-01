package ru.training.karaf.repo;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.LocationDO;
import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.SensorDO;
import ru.training.karaf.model.SensorTypeDO;
import ru.training.karaf.model.UserDO;
import ru.training.karaf.wrapper.QueryParams;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

public class SensorRepo {
    private final JpaTemplate template;
    private final Repo repo;
    private final static Class<SensorDO> CLASS = SensorDO.class;

    public SensorRepo(JpaTemplate template) {
        this.template = template;
        repo = new Repo(template);
    }

    public List<? extends Sensor> getAll(QueryParams query) {
        return repo.getAll(query, CLASS);
    }

    public long getCount(QueryParams query) {
        return repo.getCount(query, CLASS);
    }

    public Optional<? extends Sensor> create(Sensor sensor) {
        return template.txExpr(em -> {
            if (!(repo.getByName(sensor.getName(), em, CLASS).isPresent())) {
                SensorDO sensorToCreate = new SensorDO(sensor);
                em.persist(sensorToCreate);
                if (sensor.getLocation() != null) {
                    sensorToCreate.setLocation(repo.getEntityById(sensor.getLocation().getId(), em, LocationDO.class));
                }
                if (sensor.getType() != null) {
                    sensorToCreate.setType(repo.getEntityById(sensor.getType().getId(), em, SensorTypeDO.class));
                }
                sensorToCreate.setUsers(repo.getEntitySet(sensor.getUsers(), em, UserDO.class));
                return Optional.of(sensorToCreate);
            }
            throw new ValidationException("Name is already exist");
        });
    }

    public Optional<? extends Sensor> update(long id, Sensor sensor) {
        return template.txExpr(em -> {
            List<SensorDO> sensors = repo.getByIdOrName(id, sensor.getName(), em, CLASS);
            if (sensors.size() > 1) {
                throw new ValidationException("This name is already exist");
            }
            if (!sensors.isEmpty()) {
                SensorDO sensorToUpdate = sensors.get(0);
                if (sensorToUpdate.getId() == id) {
                    sensorToUpdate.setName(sensor.getName());
                    sensorToUpdate.setX(sensor.getX());
                    sensorToUpdate.setY(sensor.getY());
                    if (sensor.getLocation() != null) {
                        sensorToUpdate.setLocation(repo.getEntityById(sensor.getLocation().getId(), em, LocationDO.class));
                    }
                    if (sensor.getType() != null) {
                        sensorToUpdate.setType(repo.getEntityById(sensor.getType().getId(), em, SensorTypeDO.class));
                    }
                    sensorToUpdate.setUsers(repo.getEntitySet(sensor.getUsers(), em, UserDO.class));
                    em.merge(sensorToUpdate);
                    return Optional.of(sensorToUpdate);
                }
            }
            return Optional.empty();
        });
    }

    public Optional<? extends Sensor> get(long id) {
        return template.txExpr(TransactionType.Required, em -> repo.getById(id, em, CLASS));
    }

    public Optional<? extends Sensor> delete(long id) {
        return template.txExpr(em -> repo.getById(id, em, CLASS).map(sensorDO -> {
            if (sensorDO.getLocation() != null) {
                sensorDO.getLocation().getSensors().remove(sensorDO);
            }
            if (sensorDO.getLocation() != null) {
                sensorDO.getType().getSensors().remove(sensorDO);
            }
            sensorDO.getUsers().forEach(u -> u.getSensors().remove(sensorDO));
            em.remove(sensorDO);
            return sensorDO;
        }));
    }

    public Optional<? extends Sensor> setSensorType(long id, long typeId) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<SensorDO> sensorToUpdate = repo.getById(id, em, CLASS);
            sensorToUpdate.ifPresent(p -> {
                p.setType(repo.getEntityById(typeId, em, SensorTypeDO.class));
                em.merge(p);
            });
            return sensorToUpdate;
        });
    }

    public Optional<? extends Sensor> setLocation(long id, long locationId) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<SensorDO> sensorToUpdate = repo.getById(id, em, CLASS);
            sensorToUpdate.ifPresent(p -> {
                p.setLocation(repo.getEntityById(locationId, em, LocationDO.class));
                em.merge(p);
            });
            return sensorToUpdate;
        });
    }

    public Optional<? extends Sensor> addUsers(long id, List<Long> userIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<SensorDO> sensorToUpdate = repo.getById(id, em, CLASS);
            sensorToUpdate.ifPresent(p -> {
                p.addUsers(repo.getEntitySetByIds(userIds, em, UserDO.class));
                em.merge(p);
            });
            return sensorToUpdate;
        });
    }

    public Optional<? extends Sensor> setXY(long id, long x, long y) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<SensorDO> sensorToUpdate = repo.getById(id, em, CLASS);
            sensorToUpdate.ifPresent(p -> {
                p.setX(x);
                p.setY(y);
                em.merge(p);
            });
            return sensorToUpdate;
        });
    }
}
