package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import javax.validation.ValidationException;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.LocationDO;
import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.SensorDO;
import ru.training.karaf.model.SensorTypeDO;
import ru.training.karaf.model.UserDO;

public class SensorRepoImpl implements SensorRepo {
    private final JpaTemplate template;
    private final RepoImpl<SensorDO> sensorRepo;
    private final Class<SensorDO> stdClass = SensorDO.class;

    public SensorRepoImpl(JpaTemplate template) {
        this.template = template;
        sensorRepo = new RepoImpl<>(template, stdClass);
    }

    @Override
    public List<? extends Sensor> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz
    ) {
        return sensorRepo.getAll(by, order, field, cond, value, pg, sz);
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        return sensorRepo.getCount(field, cond, value, pg, sz);
    }

    @Override
    public List<? extends Sensor> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue) {
        return null;
    }

    @Override
    public Optional<? extends Sensor> create(Sensor sensor) {
        return template.txExpr(em -> {
            if (!(sensorRepo.getByName(sensor.getName(), em).isPresent())) {
                SensorDO sensorToCreate = new SensorDO(sensor);
                em.persist(sensorToCreate);
                if (sensor.getLocation() != null) {
                   sensorToCreate.setLocation(sensorRepo.getEntityById(sensor.getLocation().getId(), em, LocationDO.class));
                }
                if (sensor.getType() != null) {
                    sensorToCreate.setType(sensorRepo.getEntityById(sensor.getType().getId(), em, SensorTypeDO.class));
                }
                sensorToCreate.setUsers(sensorRepo.getEntitySet(sensor.getUsers(), em, UserDO.class));
                return Optional.of(sensorToCreate);
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<? extends Sensor> update(long id, Sensor sensor) {
        return template.txExpr(em -> {
            List<SensorDO> l = sensorRepo.getByIdOrName(id, sensor.getName(), em);
            if (l.size() > 1) {
                throw new ValidationException("This name is already exist");
            }
            if (!l.isEmpty()) {
                SensorDO sensorToUpdate = l.get(0);
                if (sensorToUpdate.getId() == id) {
                    sensorToUpdate.setName(sensor.getName());
                    sensorToUpdate.setX(sensor.getX());
                    sensorToUpdate.setY(sensor.getY());
                    if (sensor.getLocation() != null) {
                        sensorToUpdate.setLocation(sensorRepo.getEntityById(sensor.getLocation().getId(), em, LocationDO.class));
                    }
                    if (sensor.getType() != null) {
                        sensorToUpdate.setType(sensorRepo.getEntityById(sensor.getType().getId(), em, SensorTypeDO.class));
                    }
                    sensorToUpdate.setUsers(sensorRepo.getEntitySet(sensor.getUsers(), em, UserDO.class));
                    em.merge(sensorToUpdate);
                    return Optional.of(sensorToUpdate);
                }
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<? extends Sensor> get(long id) {
        return template.txExpr(TransactionType.Required, em -> sensorRepo.getById(id, em));
    }

    @Override
    public Optional<? extends Sensor> delete(long id) {
        return template.txExpr(em -> sensorRepo.getById(id, em).map(l -> {
            if (l.getLocation() != null) {
                l.getLocation().getSensors().remove(l);
            }
            if (l.getLocation() != null) {
                l.getType().getSensors().remove(l);
            }
            l.getUsers().forEach(u -> u.getSensors().remove(l));
            //l.getMeasurings().forEach(s -> s.setSensor(null)); //  delete cascade.ALL in SensorDO for this case
            em.remove(l);
            return l;
        }));
    }

    @Override
    public Optional<? extends Sensor> setSensorType(long id, long typeId) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<SensorDO> sensorToUpdate = sensorRepo.getById(id, em);
            sensorToUpdate.ifPresent(p -> {
                p.setType(sensorRepo.getEntityById(typeId, em, SensorTypeDO.class));
                em.merge(p);
            });
            return sensorToUpdate;
        });
    }

    @Override
    public Optional<? extends Sensor> setLocation(long id, long locationId) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<SensorDO> sensorToUpdate = sensorRepo.getById(id, em);
            sensorToUpdate.ifPresent(p -> {
                p.setLocation(sensorRepo.getEntityById(locationId, em, LocationDO.class));
                em.merge(p);
            });
            return sensorToUpdate;
        });
    }

    @Override
    public Optional<? extends Sensor> addUsers(long id, List<Long> userIds) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<SensorDO> sensorToUpdate = sensorRepo.getById(id, em);
            sensorToUpdate.ifPresent(p -> {
                p.addUsers(sensorRepo.getEntitySet(userIds, em, UserDO.class));
                em.merge(p);
            });
            return sensorToUpdate;
        });
    }

    @Override
    public Optional<? extends Sensor> setXY(long id, long x, long y) {
        return template.txExpr(TransactionType.Required, em -> {
            Optional<SensorDO> sensorToUpdate = sensorRepo.getById(id, em);
            sensorToUpdate.ifPresent(p -> {
                p.setX(x);
                p.setY(y);
                em.merge(p);
            });
            return sensorToUpdate;
        });
    }
}
