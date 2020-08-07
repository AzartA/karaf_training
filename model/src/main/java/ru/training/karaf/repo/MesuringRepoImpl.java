package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.model.Measuring;
import ru.training.karaf.model.MeasuringDO;
import ru.training.karaf.model.SensorDO;

public class MesuringRepoImpl implements MeasuringRepo {
    private final JpaTemplate template;
    private final RepoImpl<MeasuringDO> repo;
    private final Class<MeasuringDO> stdClass = MeasuringDO.class;

    public MesuringRepoImpl(JpaTemplate template) {
        this.template = template;
        repo= new RepoImpl<>(template, stdClass);
    }

    @Override
    public List<? extends Measuring> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue) {
        return null;
    }

    @Override
    public List<? extends Measuring> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz
    ) {
        return repo.getAll(by, order, field, cond, value, pg, sz);
    }

    @Override
    public Optional<? extends Measuring> create(Measuring measuring) {
        return template.txExpr(em -> {
            MeasuringDO measuringToCreate = new MeasuringDO(measuring.getValue());
            em.persist(measuringToCreate);
            if (measuring.getParameter() != null) {
                measuringToCreate.setParameter(repo.getEntityById(measuring.getParameter().getId(), em, ClimateParameterDO.class));
            }
           /* if (measuring.getTimestamp() != null) {
                measuringToCreate.setTimestamp(measuring.getTimestamp());
            }*/
            if (measuring.getSensor() != null) {
                measuringToCreate.setSensor(repo.getEntityById(measuring.getSensor().getId(), em, SensorDO.class));
            }
            em.flush();
            em.refresh(measuringToCreate);
            return Optional.of(measuringToCreate);
        });
    }

    @Override
    public Optional<? extends Measuring> update(long id, Measuring measuring) {
        return template.txExpr(em -> repo.getById(id, em)
                    .map(m -> {
                        m.setValue(measuring.getValue());
                        if(measuring.getSensor()!=null) {
                            m.setSensor(repo.getEntityById(measuring.getSensor().getId(),em,SensorDO.class));
                        }
                        if(measuring.getParameter()!=null) {
                            m.setParameter(repo.getEntityById(measuring.getParameter().getId(),em,ClimateParameterDO.class));
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
