package com.intexsoft.repository.jparepository;

import com.intexsoft.model.CommonModel;
import com.intexsoft.repository.CommonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public abstract class JpaCommonRepository<E extends CommonModel<I, E>, I extends Serializable> implements CommonRepository<E, I> {

    @Override
    public I getGeneratedId(E e) {
        return e.getId();
    }

    @Autowired
    private EntityManager em;

    protected abstract Class<E> getModelClass();

    @Override
    @Transactional
    public E getById(I id) {
        return em.find(getModelClass(), id);

    }

    protected E getById(I id, List<String> fetchCriterias) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getModelClass());
        Root<E> from = query.from(getModelClass());
        Fetch<?, ?> fetchPath = null;
        for (String path : fetchCriterias) {
            for (String criteria : path.split(",")) {
                if (fetchPath == null) {
                    fetchPath = from.fetch(criteria, JoinType.LEFT);
                } else {
                    fetchPath = fetchPath.fetch(criteria, JoinType.LEFT);
                }
            }
        }
        query.select(from).where(cb.equal(from.get("id"), id));
        try {
            return em.createQuery(query).getSingleResult();
        } catch (NoResultException nre) {
            log.warn("Object not found: ", nre);
            return null;
        }
    }

    @Override
    @Transactional
    public E save(E e) {
        I id = getGeneratedId(e);
        em.persist(e.setId(id)
                .setVersion(1));
        return getById(id);
    }

    @Override
    @Transactional
    public E update(E e) {
        em.merge(e);
        return getById(e.getId());
    }

    @Override
    @Transactional
    public void deleteById(I id) {
        em.remove(getById(id));
    }

    @Override
    @Transactional
    public List<E> search(Map<String, Object> searchCriterias) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(getModelClass());
        Root<E> from = cq.from(getModelClass());
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> entry : searchCriterias.entrySet()) {
            if (!Objects.equals(entry.getValue(), null))
                predicates.add(cb.equal(from.get(entry.getKey()), entry.getValue()));
        }
        cq.select(from).where(predicates.toArray(new Predicate[]{}));
        return em.createQuery(cq).getResultList();
    }
}
