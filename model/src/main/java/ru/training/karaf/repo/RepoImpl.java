package ru.training.karaf.repo;

import java.lang.reflect.Field;
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
import javax.persistence.criteria.Root;
import javax.validation.ValidationException;

import org.apache.aries.jpa.template.JpaTemplate;

import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.ClimateParameterDO;
import ru.training.karaf.model.Entity;


public class RepoImpl<T extends Entity> {//implements Repo<T> {
    private final JpaTemplate template;

    public RepoImpl(JpaTemplate template) {
        this.template = template;
     }

   public List<? extends T> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue, Class<T> enClass) {
        return template.txExpr(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cr = cb.createQuery(enClass);
            Root<T> root = cr.from(enClass);
            cr.select(root);
            List<String> fieldNames = Arrays.asList(enClass.getDeclaredFields()).stream().map(Field::getName).collect(Collectors.toList());
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

            TypedQuery query = em.createQuery(cr);

            if (pg > 0 && sz > 0) {
                int offset = (pg - 1) * sz;
                query.setFirstResult(offset)
                        .setMaxResults(sz);
            }
            return query.getResultList();
        });
    }





    public Optional<? extends T> delete(long id) {
        return Optional.empty();
    }

    public Optional<T> getById(long id, EntityManager em, Class<T> t) {
        return Optional.ofNullable(em.find(t, id));
    }

    public Set<T> getEntitySet(List<Long> ids, EntityManager em, Class<T> t) {
        Set<T> ts = new HashSet<>(4);
        ids.forEach(id -> getById(id,em,t).ifPresent(entity -> ts.add(entity)));
        return ts;
    }

    public Optional<T> getByName(String name, EntityManager em, Class<T> t) {
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

    public List<T> getByIdOrName(long id, String name, EntityManager em, Class<T> t) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cr = cb.createQuery(t);
        Root<T> root = cr.from(t);
        cr.select(root);
        cr.where(cb.or(cb.equal(root.get("name"), name),cb.equal(root.get("id"), id)));
        return em.createQuery(cr).getResultList();
    }
}
