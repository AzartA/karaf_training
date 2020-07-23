package ru.training.karaf.repo;

import org.apache.aries.jpa.template.JpaTemplate;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class UniqueValidationRepoImpl implements UniqueValidationRepo {
    private final JpaTemplate template;

    public UniqueValidationRepoImpl(JpaTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<Object> presentObject(Class<?> entityClass, String filedName, Object fieldValue) {
        return template.txExpr(em -> getByUniqueFiled(entityClass, filedName, fieldValue, em));
    }

    private Optional<Object> getByUniqueFiled(Class<?> entityClass, String filedName, Object fieldValue, EntityManager em) {
        final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        final CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        final Root<?> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(filedName), fieldValue));
        try {
            return Optional.of(em.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
