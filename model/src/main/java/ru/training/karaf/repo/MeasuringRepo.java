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
            em.refresh(measuringToCreate);
            return Optional.of(measuringToCreate);
        });
    }


    public Optional<? extends Measuring> update(long id, Measuring measuring) {
        return template.txExpr(em -> repo.getById(id, em, MeasuringDO.class)
                .map(measuringDO -> {
                    measuringDO.setValue(measuring.getValue());
                    if (measuring.getSensor() != null) {
                        measuringDO.setSensor(repo.getEntityById(measuring.getSensor().getId(), em, SensorDO.class));
                    }
                    if (measuring.getParameter() != null) {
                        measuringDO.setParameter(repo.getEntityById(measuring.getParameter().getId(), em, ClimateParameterDO.class));
                    }
                    em.merge(measuringDO);
                    return measuringDO;
                }));
    }


    public Optional<? extends Measuring> get(long id) {
        return Optional.ofNullable(template.txExpr(em -> em.find(MeasuringDO.class, id)));
    }


    public Optional<? extends Measuring> delete(long id) {
        return template.txExpr(em -> repo.getById(id, em, MeasuringDO.class).map(measuringDO -> {
                    if (measuringDO.getParameter() != null) {
                        measuringDO.getParameter().getMeasurings().remove(measuringDO);
                    }
                    if (measuringDO.getSensor() != null) {
                        measuringDO.getSensor().getMeasurings().remove(measuringDO);
                    }
                    em.remove(measuringDO);
                    return measuringDO;
                })
        );
    }
}
