package ru.training.karaf.repo;

import java.util.ArrayList;
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
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.Entity;
import ru.training.karaf.view.FilterParam;
import ru.training.karaf.view.SortParam;
import ru.training.karaf.wrapper.QueryParams;

public class Repo {
    private final JpaTemplate template;

    public Repo(JpaTemplate template) {
        this.template = template;
    }

    public <T> long getCount(QueryParams queryParams, Class<T> t) {

        return template.txExpr(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cr = cb.createQuery(Long.class);
            Root<T> root = cr.from(t);
            cr.select(root.get("id"));
            //filtering
            List<Predicate> predicates = filtering(queryParams.getFilters(), cb, root);
            cr.where(cb.and(predicates.toArray(new Predicate[0])));
            TypedQuery<Long> query = em.createQuery(cr);
            //pagination
            pagination(queryParams.getPagination()[0], queryParams.getPagination()[1], query);
            return (long) query.getResultList().size();
        });
    }

    public <T> List<? extends T> getAll(QueryParams queryParams, Class<T> t) {
        return template.txExpr(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cr = cb.createQuery(t);
            Root<T> root = cr.from(t);
            cr.select(root);
            //filtering
            List<Predicate> predicates = filtering(queryParams.getFilters(), cb, root);
            cr.where(cb.and(predicates.toArray(new Predicate[0])));
            //sorting
            List<Order> orders = getOrders(queryParams.getSorts(), cb, root);
            cr.orderBy(orders);
            TypedQuery<T> query = em.createQuery(cr);
            //pagination
            pagination(queryParams.getPagination()[0], queryParams.getPagination()[1], query);
            return query.getResultList();
        });
    }

    private <T> List<Order> getOrders(List<SortParam> sorts, CriteriaBuilder cb, Root<T> root) {
        List<Order> orders = new ArrayList<>();
        for (SortParam sort : sorts) {
            Path<String> path = getPath(sort.getBy(), root);
            Order ord = "desc".equalsIgnoreCase(sort.getOrder()) ? cb.desc(path) : cb.asc(path);
            orders.add(ord);
        }
        return orders;
    }

    private void pagination(int pg, int sz, TypedQuery<?> query) {
        int offset = (pg - 1) * sz;
        if (pg == 0 ) {
            offset = 0;
        }
        query.setFirstResult(offset)
                .setMaxResults(sz);

    }

    private <T> List<Predicate> filtering(
            List<FilterParam> filterParams, CriteriaBuilder cb, Root<T> root
    ) {
        List<Predicate> predicates = new ArrayList<>();
        for (FilterParam filter : filterParams) {
            Class<?> type = root.getJavaType();
            Path<String> path = getPath(filter.getField(), root);
            Predicate predicate = getPredicate(filter.getCond(), filter.getValue(), path, type, cb);
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
              /*  if (!type.equals(String.class)) {
                    throw new ValidationException("The condition 'contains' incompatible with type of the field.");
                }*/
                return cb.like(path, "%" + value + "%");
            case "!contains":
               /* if (!type.equals(String.class)) {
                    throw new ValidationException("The condition 'contains' incompatible with type of the field.");
                }*/
                return cb.not(cb.like(path, "%" + value + "%"));
            case "!=":
                return cb.not(cb.equal(path, value));
            case "=":
            default:
                return cb.equal(path, value);
        }
    }

    private <T> Path<String> getPath(String field, Root<T> root) {
        String[] fieldParts = (field).split("\\.");
        Class<?> type = root.getJavaType();
        //verification
        /*for (String fieldPart : fieldParts) {
            try {
                if (fieldPart.endsWith("s")) {
                    Field fld = type.getDeclaredField(fieldPart);
                    ParameterizedType pType = (ParameterizedType) fld.getGenericType();
                    type = (Class<?>) pType.getActualTypeArguments()[0];
                } else {
                    type = type.getDeclaredField(fieldPart).getType();
                }
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException("There is no such field: "+ fieldPart);
            }
        }*/


        From<String, String> joinPath = null;
        Path<String> path = null;
        boolean[] pattern = new boolean[fieldParts.length];
        for (int j = fieldParts.length - 2; j >= 0; j--) {
            pattern[j] = fieldParts[j].endsWith("s") || pattern[j + 1];
        }

        for (int j = 0; j < fieldParts.length; j++) {
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
