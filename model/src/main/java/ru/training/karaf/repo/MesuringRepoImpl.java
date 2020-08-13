package ru.training.karaf.repo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.model.Location;
import ru.training.karaf.model.Measuring;
import ru.training.karaf.model.MeasuringDO;
import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.SensorDO;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CollectionJoin;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.validation.ValidationException;

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
        //return repo.getAll(by, order, field, cond, value, pg, sz);
        return template.txExpr(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<MeasuringDO> cr = cb.createQuery(stdClass);
            Root<MeasuringDO> root = cr.from(stdClass);
            cr.select(root);
            //List<String> fieldNames = Arrays.stream(stdClass.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
            //filtering
            if (field != null && cond != null && value != null &&
                    field.size() == cond.size() && field.size() == value.size()) {
                List<Predicate> predicates = new ArrayList<>();
                for (int i = 0; i < field.size(); i++) {
                    String[] fparts = (field.get(i)).split("\\.");
                    Expression<String> path;
                        switch (fparts.length){
                            case 1:
                                path = root.get(fparts[0]);
                            break;
                            case 2:
                                path = root.get(fparts[0]).get(fparts[1]);
                            break;
                            case 3:
                                if("users".equals(fparts[1])) {
                                    path = root.join(fparts[0]).join(fparts[1]).get(fparts[2]);
                                }else{
                                    path = root.get(fparts[0]).get(fparts[1]).get(fparts[2]);
                                }
                            break;
                            default:
                                path = root.get(fparts[0]);
                                break;
                        }
                    switch (cond.get(i)) {
                        case ">":
                            predicates.add(cb.greaterThanOrEqualTo(path, value.get(i)));
                            break;
                        case "<":
                            predicates.add(cb.lessThanOrEqualTo(path, value.get(i)));
                            break;
                        case "contain":
                            predicates.add(cb.like(path, "%" + value.get(i) + "%"));
                            break;
                        case "equals":
                        default:
                            predicates.add(cb.equal(path, value.get(i)));
                            break;
                    }
                }
                Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
                cr.where(cb.and(predicateArray));
            }
            //sorting
            if (by != null && order != null &&
                    by.size() == order.size()) {
                List<Order> orders = new ArrayList<>();
                for (int i = 0; i < by.size(); i++) {
                    String[] fparts = (by.get(i)).split("\\.");
                    Expression<String> path;
                    switch (fparts.length){
                        case 1:
                            path = root.get(fparts[0]);
                            break;
                        case 2:
                            path = root.get(fparts[0]).get(fparts[1]);
                            break;
                        case 3:
                            if("users".equals(fparts[1])) {
                                path = root.join(fparts[0]).join(fparts[1]).get(fparts[2]);
                            }else{
                                path = root.get(fparts[0]).get(fparts[1]).get(fparts[2]);
                            }
                            break;
                        default:
                            path = root.get(fparts[0]);
                            break;
                    }
                    if ("asc".equalsIgnoreCase(order.get(i))) {
                        orders.add(cb.asc(path));
                    }
                    if ("desc".equalsIgnoreCase(order.get(i))) {
                        orders.add(cb.desc(path));
                    }
                }
                cr.orderBy(orders);
            }
            TypedQuery<MeasuringDO> query = em.createQuery(cr);
            //pagination
            if (pg > 0 && sz > 0) {
                int offset = (pg - 1) * sz;
                query.setFirstResult(offset)
                        .setMaxResults(sz);
            }
            return query.getResultList();
        });



    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        return repo.getCount(field, cond, value, pg, sz);
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
