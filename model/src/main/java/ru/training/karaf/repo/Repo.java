package ru.training.karaf.repo;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.Entity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ValidationException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Repo {
    private final JpaTemplate template;

    public Repo(JpaTemplate template) {
        this.template = template;
    }

    public <T> long getCount(
            List<String> field, List<String> cond, List<String> value, int pg, int sz,
            String[] auth, Class<T> t
    ) {

        return template.txExpr(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cr = cb.createQuery(Long.class);
            Root<T> root = cr.from(t);

            cr.select(root.get("id"));
            //filtering
            if (field != null && cond != null && value != null &&
                    field.size() == cond.size() && field.size() == value.size()) {
                List<Predicate> predicates = filtering(field, cond, value, cb, root);
                //auth filter
                if (!auth[0].isEmpty()) {
                    predicates.addAll(filtering(Collections.singletonList(auth[0]), Arrays.asList("="), Arrays.asList(auth[1]), cb, root));
                }
                cr.where(cb.and(predicates.toArray(new Predicate[0])));
            }
            TypedQuery<Long> query = em.createQuery(cr);
            //pagination
            pagination(pg, sz, query);
            return (long) query.getResultList().size();
        });
    }

    public <T> List<? extends T> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz, String[] auth, Class<T> t
    ) {
        return template.txExpr(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cr = cb.createQuery(t);
            Root<T> root = cr.from(t);
            cr.select(root);
            //filtering
            if (field != null && cond != null && value != null &&
                    field.size() == cond.size() && field.size() == value.size()) {
                List<Predicate> predicates = filtering(field, cond, value, cb, root);
                //auth filter
                if (auth[0]!=null) {
                    predicates.addAll(filtering(Collections.singletonList(auth[0]), Arrays.asList("="), Arrays.asList(auth[1]), cb, root));
                }
                cr.where(cb.and(predicates.toArray(new Predicate[0])));
            }
            //sorting
            if (by != null && order != null &&
                    by.size() == order.size()) {
                List<Order> orders = getOrders(by, order, cb, root);
                cr.orderBy(orders);
            }
            TypedQuery<T> query = em.createQuery(cr);
            //pagination
            pagination(pg, sz, query);
            return query.getResultList();
        });
    }

    private <T> List<Order> getOrders(List<String> by, List<String> order, CriteriaBuilder cb, Root<T> root) {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < by.size(); i++) {
            Class<?> type = root.getJavaType();
            Path<String> path = getPath(by.get(i), root, type);
            Order ord = "desc".equalsIgnoreCase(order.get(i)) ? cb.desc(path) : cb.asc(path);
            orders.add(ord);
        }
        return orders;
    }

    private void pagination(int pg, int sz, TypedQuery<?> query) {
        if (pg > 0 && sz > 0) {
            int offset = (pg - 1) * sz;
            query.setFirstResult(offset)
                    .setMaxResults(sz);
        }
    }

    private <T> List<Predicate> filtering(
            List<String> field, List<String> cond, List<String> value,
            CriteriaBuilder cb, Root<T> root
    ) {
        List<Predicate> predicates = new ArrayList<>();
        for (int i = 0; i < field.size(); i++) {
            Class<?> type = root.getJavaType();
            Path<String> path = getPath(field.get(i), root, type);
            Predicate predicate = getPredicate(cond.get(i), value.get(i), path, type, cb);
            predicates.add(predicate);
        }
        return predicates;
    }

    private Predicate getPredicate(String condition, String value, Path<String> path, Class<?> type, CriteriaBuilder cb) {
        switch (condition) {
            case ">":
                return cb.greaterThan(path, value);
            case "<":
                return cb.lessThan(path, value);
            case ">=":
                return cb.greaterThanOrEqualTo(path, value);
            case "<=":
                return cb.lessThanOrEqualTo(path, value);
            case "contains":
                if (!type.equals(String.class)) {
                    throw new ValidationException("The condition 'contains' incompatible with type of the field.");
                }
                return cb.like(path, "%" + value + "%");
            case "!contains":
                if (!type.equals(String.class)) {
                    throw new ValidationException("The condition 'contains' incompatible with type of the field.");
                }
                return cb.not(cb.like(path, "%" + value + "%"));
            case "!=":
                return cb.not(cb.equal(path, value));
            case "=":
            default:
                return cb.equal(path, value);
        }
    }

    private <T> Path<String> getPath(String field, Root<T> root, Class<?> type) {
        String[] fieldParts = (field).split("\\.");
        From<String, String> joinPath = null;
        Path<String> path = null;
        boolean[] pattern = new boolean[fieldParts.length];
        for (int j = fieldParts.length - 2; j >= 0; j--) {
            pattern[j] = fieldParts[j].endsWith("s") || pattern[j + 1];
        }

        for (int j = 0; j < fieldParts.length; j++) {
            //verification
            try {
                if (fieldParts[j].endsWith("s")) {
                    Field fld = type.getDeclaredField(fieldParts[j]);
                    ParameterizedType pType = (ParameterizedType) fld.getGenericType();
                    type = (Class<?>) pType.getActualTypeArguments()[0];
                } else {
                    type = type.getDeclaredField(fieldParts[j]).getType();
                }
            } catch (NoSuchFieldException e) {
                throw new ValidationException("There is no such field: " + fieldParts[j]);
            }
            //build path
            if (pattern[j]) {
                if (j == 0) {
                    joinPath = root.join(fieldParts[0]);
                } else {
                    joinPath = joinPath.join(fieldParts[j]);
                }
            } else {
                if (j == 0) {
                    path = root.get(fieldParts[0]);
                } else {
                    if (pattern[j - 1]) {
                        path = joinPath.get(fieldParts[j]);
                    } else {
                        path = path.get(fieldParts[j]);
                    }
                }
            }
        }
        return path;
    }

    public <T> Optional<T> getById(long id, EntityManager em, Class<T> t) {
        return Optional.ofNullable(em.find(t, id));
    }

    public <U> U getEntityById(long id, EntityManager em, Class<U> t) {
        return em.find(t, id);
    }

    public <U> Set<U> getEntitySetByIds(List<Long> ids, EntityManager em, Class<U> u) {
        Set<U> ts = new HashSet<>(4);
        ids.forEach(id -> Optional.ofNullable(getEntityById(id, em, u)).ifPresent(ts::add));
        return ts;
    }

    public <U> Set<U> getEntitySet(Set<? extends Entity> entitySet, EntityManager em, Class<U> u) {
        Set<U> entities = new HashSet<>(4);
        if (entitySet != null) {
            List<Long> entityIds = entitySet.stream().map(Entity::getId).collect(Collectors.toList());
            entities = getEntitySetByIds(entityIds, em, u);
        }
        return entities;
    }

    public <T> Optional<T> getByName(String name, EntityManager em, Class<T> t) {
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

    public <T> List<T> getByIdOrName(long id, String name, EntityManager em, Class<T> t) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cr = cb.createQuery(t);
        Root<T> root = cr.from(t);
        cr.select(root);
        cr.where(cb.or(cb.equal(root.get("name"), name), cb.equal(root.get("id"), id)));
        return em.createQuery(cr).getResultList();
    }
}
