package ru.training.karaf.repo;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ValidationException;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.Entity;

public class RepoImpl<T extends Entity> implements Repo<T> {
    private final JpaTemplate template;
    //private final Class<T> enClass;
    //private final UnitRepoIml unitRepo;

    public RepoImpl(JpaTemplate template) {
        this.template = template;
        //unitRepo = new UnitRepoIml(template);
        //this.enClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public List<? extends T> getAll(String sortBy, String sortOrder, int pg, int sz, String filterField, String filterValue) {
        return null;
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

    @Override
    public Optional<? extends T> create(T entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends T> update(long id, T entity) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends T> get(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<? extends T> delete(long id) {
        return Optional.empty();
    }
}
