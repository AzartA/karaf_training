package ru.training.karaf.repo;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ValidationException;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.Entity;

public class RepoImpl<T extends Entity> {//implements Repo<T> {
    private final JpaTemplate template;
    private final Class<T> t;

    public RepoImpl(JpaTemplate template, Class<T> t) {
        this.template = template;
        this.t = t;
    }

    public List<? extends T> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue) {
        return template.txExpr(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cr = cb.createQuery(t);
            Root<T> root = cr.from(t);
            cr.select(root);
            List<String> fieldNames = Arrays.stream(t.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
            if (filterField != null && filterValue != null) {
                if (!fieldNames.contains(filterField)) {
                    throw new ValidationException("There is no such field: " + filterField);
                }
                cr.where(cb.equal(root.get(filterField), filterValue));
            }
            if (sortBy != null && sortOrder != null) {
                if (!fieldNames.contains(sortBy)) {
                    throw new ValidationException("There is no such field: " + sortBy);
                }
                if ("asc".equalsIgnoreCase(sortOrder)) {
                    cr.orderBy(cb.asc(root.get(sortBy)));
                }
                if ("desc".equalsIgnoreCase(sortOrder)) {
                    cr.orderBy(cb.desc(root.get(sortBy)));
                }
            }

            TypedQuery<T> query = em.createQuery(cr);

            if (pg > 0 && sz > 0) {
                int offset = (pg - 1) * sz;
                query.setFirstResult(offset)
                        .setMaxResults(sz);
            }
            return query.getResultList();
        });
    }

    public List<? extends T> getAll(
            List<String> by, List<String> order,
            List<String> field, List<String> cond, List<String> value,
            int pg, int sz
    ) {

        return template.txExpr(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cr = cb.createQuery(t);
            Root<T> root = cr.from(t);
            cr.select(root);
            List<String> fieldNames = Arrays.stream(t.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
            //filtering
            if (field != null && cond != null && value != null &&
                    field.size() == cond.size() && field.size() == value.size()) {
                List<Predicate> predicates = new ArrayList<>();
                for (int i = 0; i < field.size(); i++) {
                    Expression<String> path;
                    try {
                        Class<?> fType = t.getDeclaredField(field.get(i)).getType();
                        if (!(fType.equals(String.class) || fType.equals(Long.TYPE) || fType.equals(Float.TYPE) )) {
                            path = root.get(field.get(i)).get("name");
                        } else {
                            path = root.get(field.get(i));
                        }
                    } catch (NoSuchFieldException | SecurityException e) {
                        throw new ValidationException("There is no such field: " + field.get(i));
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
                        /*case "like":
                            predicates.add(cb.like(path, value.get(i)));
                            break;*/
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

                    if (!fieldNames.contains(by.get(i))) {
                        throw new ValidationException("There is no such field: " + by.get(i));
                    }
                    Expression<String> path;
                    try {
                        Class<?> fType = t.getDeclaredField(by.get(i)).getType();

                        if (!(fType.equals(String.class) || fType.equals(Long.TYPE) || fType.equals(Float.TYPE))) {
                            path = root.get(by.get(i)).get("name");
                        } else {
                            path = root.get(by.get(i));
                        }
                    } catch (NoSuchFieldException | SecurityException e) {
                        throw new ValidationException("There is no such field: " + by.get(i));
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
            TypedQuery<T> query = em.createQuery(cr);
            //pagination
            if (pg > 0 && sz > 0) {
                int offset = (pg - 1) * sz;
                query.setFirstResult(offset)
                        .setMaxResults(sz);
            }
            return query.getResultList();
        });
    }

    public Optional<T> getById(long id, EntityManager em) {
        return Optional.ofNullable(em.find(t, id));
    }

    public <U> U getEntityById(long id, EntityManager em, Class<U> t) {
        return em.find(t, id);
    }

    public <U> Set<U> getEntitySet(List<Long> ids, EntityManager em, Class<U> u) {
        Set<U> ts = new HashSet<>(4);
        ids.forEach(id -> Optional.ofNullable(getEntityById(id, em, u)).ifPresent(ts::add));
        return ts;
    }

    public <U> Set<U> getEntitySet(Set<? extends Entity> entitySet, EntityManager em, Class<U> u) {
        Set<U> entities = new HashSet<>(4);
        if (entitySet != null) {
            List<Long> entityIds = entitySet.stream().map(Entity::getId).collect(Collectors.toList());
            entities = getEntitySet(entityIds, em, u);
        }
        return entities;
    }

    public Optional<T> getByName(String name, EntityManager em) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cr = cb.createQuery(t);
            Root<T> root = cr.from(t);
            cr.select(root);
            cr.where(cb.equal(root.get("name"), name));
            return Optional.of(em.createQuery(cr).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<T> getByIdOrName(long id, String name, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cr = cb.createQuery(t);
        Root<T> root = cr.from(t);
        cr.select(root);
        cr.where(cb.or(cb.equal(root.get("name"), name), cb.equal(root.get("id"), id)));
        return em.createQuery(cr).getResultList();
    }
}
