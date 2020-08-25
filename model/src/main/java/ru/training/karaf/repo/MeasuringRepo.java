package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.model.Measuring;
import ru.training.karaf.model.MeasuringDO;
import ru.training.karaf.model.SensorDO;
import ru.training.karaf.wrapper.QueryParams;

public class MeasuringRepo {
    private final JpaTemplate template;
    private final Repo repo;
    private static final Class<MeasuringDO> CLASS = MeasuringDO.class;

    public MeasuringRepo(JpaTemplate template) {
        this.template = template;
        repo = new Repo(template);
    }


    public List<? extends Measuring> getAll(QueryParams query ) {
        return repo.getAll(query, CLASS);
    }


    public long getCount(QueryParams query) {
        return repo.getCount(query, CLASS);
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
