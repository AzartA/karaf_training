package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.model.Measuring;
import ru.training.karaf.model.MeasuringDO;
import ru.training.karaf.model.SensorDO;

public class MeasuringRepo {
    private final JpaTemplate template;
    private final Repo repo;
    private final Class<MeasuringDO> stdClass = MeasuringDO.class;

    public MeasuringRepo(JpaTemplate template) {
        this.template = template;
        repo = new Repo(template);
    }


    public List<? extends Measuring> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz,
            String[] auth
    ) {
        return null;//repo.getAll(by, order, field, cond, value, pg, sz, auth, stdClass);
    }


    public long getCount(
            List<String> field, List<String> cond, List<String> value, int pg, int sz,
            String[] auth
    ) {
        return 0;//repo.getCount(field, cond, value, pg, sz, auth, stdClass);
    }


    public Optional<? extends Measuring> create(Measuring measuring) {
        return template.txExpr(em -> {
            MeasuringDO measuringToCreate = new MeasuringDO(measuring.getValue());
            em.persist(measuringToCreate);
            if (measuring.getParameter() != null) {
                measuringToCreate.setParameter(repo.getEntityById(measuring.getParameter().getId(), em, ClimateParameterDO.class));
            }
            if (measuring.getSensor() != null) {
                measuringToCreate.setSensor(repo.getEntityById(measuring.getSensor().getId(), em, SensorDO.class));
            }
            em.flush();
            //em.setProperty("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS); // em without JPA cache
            em.refresh(measuringToCreate);
            return Optional.of(measuringToCreate);
        });
    }


    public Optional<? extends Measuring> update(long id, Measuring measuring) {
        return template.txExpr(em -> repo.getById(id, em, MeasuringDO.class)
                .map(m -> {
                    m.setValue(measuring.getValue());
                    if (measuring.getSensor() != null) {
                        m.setSensor(repo.getEntityById(measuring.getSensor().getId(), em, SensorDO.class));
                    }
                    if (measuring.getParameter() != null) {
                        m.setParameter(repo.getEntityById(measuring.getParameter().getId(), em, ClimateParameterDO.class));
                    }
                    em.merge(m);
                    return m;
                }));
    }


    public Optional<? extends Measuring> get(long id) {
        return Optional.ofNullable(template.txExpr(em -> em.find(MeasuringDO.class, id)));
    }


    public Optional<? extends Measuring> delete(long id) {
        return template.txExpr(em -> repo.getById(id, em, MeasuringDO.class).map(l -> {
                    if (l.getParameter() != null) {
                        l.getParameter().getMeasurings().remove(l);
                    }
                    if (l.getSensor() != null) {
                        l.getSensor().getMeasurings().remove(l);
                    }
                    em.remove(l);
                    return l;
                })
        );
    }
}
