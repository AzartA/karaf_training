package ru.training.karaf.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.ClimateParameterDO;
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
            filtering(field, cond, value, cb, root, cr);
            //sorting
            if (by != null && order != null &&
                    by.size() == order.size()) {
                List<Order> orders = new ArrayList<>();
                for (int i = 0; i < by.size(); i++) {
                    String[] fieldParts = (by.get(i)).split("\\.");
                    Expression<String> path;
                    switch (fieldParts.length) {
                        case 2:
                            path = root.get(fieldParts[0]).get(fieldParts[1]);
                            break;
                        case 3:
                            if ("users".equals(fieldParts[1])) {
                                path = root.join(fieldParts[0]).join(fieldParts[1]).get(fieldParts[2]);
                            } else {
                                path = root.get(fieldParts[0]).get(fieldParts[1]).get(fieldParts[2]);
                            }
                            break;
                        case 1:
                        default:
                            path = root.get(fieldParts[0]);
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
            pagination(pg, sz, query);
            return query.getResultList();
        });
    }

    private void pagination(int pg, int sz, TypedQuery<?> query) {
        if (pg > 0 && sz > 0) {
            int offset = (pg - 1) * sz;
            query.setFirstResult(offset)
                    .setMaxResults(sz);
        }
    }

    @Override
    public long getCount(List<String> field, List<String> cond, List<String> value, int pg, int sz) {
        //return repo.getCount(field, cond, value, pg, sz);
        return template.txExpr(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cr = cb.createQuery(Long.class);
            Root<MeasuringDO> root = cr.from(stdClass);
            //List<String> fieldNames = Arrays.stream(stdClass.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
            //filtering
            filtering(field, cond, value, cb, root, cr);
            cr.select(root.get("id"));
            TypedQuery<Long> query = em.createQuery(cr);
            //pagination
            pagination(pg, sz, query);
            return (long) query.getResultList().size();

            // return query.getSingleResult();
        });
    }

    private void filtering(List<String> field, List<String> cond, List<String> value, CriteriaBuilder cb, Root<MeasuringDO> root,
                           CriteriaQuery<?> cr) {
        if (field != null && cond != null && value != null &&
                field.size() == cond.size() && field.size() == value.size()) {
            List<Predicate> predicates = new ArrayList<>();
            for (int i = 0; i < field.size(); i++) {
                String[] fieldParts = (field.get(i)).split("\\.");
                Expression<String> path;
                switch (fieldParts.length) {
                    case 2:
                        path = root.get(fieldParts[0]).get(fieldParts[1]);
                        break;
                    case 3:
                        if ("users".equals(fieldParts[1])) {
                            path = root.join(fieldParts[0]).join(fieldParts[1]).get(fieldParts[2]);
                        } else {
                            path = root.get(fieldParts[0]).get(fieldParts[1]).get(fieldParts[2]);
                        }
                        break;
                    case 1:
                    default:
                        path = root.get(fieldParts[0]);
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
