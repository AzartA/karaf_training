package ru.training.karaf.repo;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ValidationException;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.Measuring;
import ru.training.karaf.model.MeasuringDO;
import ru.training.karaf.model.SensorDO;

public class MeasuringRepoImpl implements MeasuringRepo {
    private final JpaTemplate template;
    private final RepoImpl<MeasuringDO> repo;
    private final Class<MeasuringDO> stdClass = MeasuringDO.class;

    public MeasuringRepoImpl(JpaTemplate template) {
        this.template = template;
        repo = new RepoImpl<>(template, stdClass);
    }

    @Override
    public List<? extends Measuring> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz,
            String[] auth
    ) {
        return repo.getAll(by, order, field, cond, value, pg, sz, auth);
    }


    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz,
                         String[] auth) {
        return repo.getCount(field, cond, value, pg, sz, auth);
    }

    @Override
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

    @Override
    public Optional<? extends Measuring> update(long id, Measuring measuring) {
        return template.txExpr(em -> repo.getById(id, em)
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

    @Override
    public Optional<? extends Measuring> get(long id) {
        return Optional.ofNullable(template.txExpr(em -> em.find(MeasuringDO.class, id)));
    }

    @Override
    public Optional<? extends Measuring> delete(long id) {
        return template.txExpr(em -> repo.getById(id, em).map(l -> {
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
