package com.intexsoft.repository.jparepository;

import com.intexsoft.model.CommonModel;
import com.intexsoft.repository.CommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class JpaCommonRepository<E extends CommonModel<I, E>, I extends Serializable> implements CommonRepository<E, I> {

    @Override
    public I getGeneratedId(E e) {
        return e.getId();
    }

    @Autowired
    private EntityManager em;

    protected abstract Class<E> getModelClass();

    @Override
    public E getById(I id) {
        return em.find(getModelClass(), id);

    }

    protected E getById(I id, String fetch1, String fetch2) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getModelClass());
        Root<E> from = query.from(getModelClass());
        from.fetch(fetch1).fetch(fetch2);
        query.where(cb.equal(from.get("id"), id));
        return em.createQuery(query).getSingleResult();
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
    public E update(E e) {
        em.merge(e);
        return getById(e.getId());
    }

    @Override
    public void deleteById(I id) {
        em.remove(getById(id));
    }

    @Override
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
