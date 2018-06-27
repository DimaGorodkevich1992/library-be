package com.intexsoft.repository.jsonrepository;

import com.intexsoft.model.CommonModel;
import com.intexsoft.repository.CommonRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonRelation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Slf4j
public abstract class JsonCommonRepository<E extends CommonModel<I, E>, I extends Serializable> implements CommonRepository<E, I> {

    @Override
    public I getGeneratedId(E e) {
        return e.getId();
    }

    protected abstract List<E> getData();

    @Override
    public E getById(I id) {
        return getData()
                .stream()
                .filter(b -> Objects.equals(b.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public E save(E e) {
        getData().add(e
                .setId(getGeneratedId(e))
                .setVersion(1));
        return getById(e.getId());
    }

    @Override
    public E update(E e) {
        E var = getById(e.getId());
        synchronized (var) {
            if (var.getVersion() == e.getVersion()) {
                try {
                    BeanUtils.copyProperties(var, e);
                    var.setVersion(var.getVersion() + 1);
                } catch (ReflectiveOperationException ex) {
                    log.error("Failed to copy properties", e.getId(), ex);
                    throw new RuntimeException("failed to copy properties " + e.getId(), ex);
                }
            } else {
                log.error("Version mismatch!!! you version " +
                        e.toString() + " last version " + var.toString());
                throw new IllegalArgumentException("version mismatch!!! you version " +
                        e.toString() + " last version " + var.toString());
            }
        }
        return var;
    }

    @Override
    public List<E> search(Map<String, Object> searchCriterias) {
        return getData()
                .stream()
                .filter(entry -> searchCriterias
                        .entrySet()
                        .stream()
                        .allMatch(searchCriteria -> getCriteriaForSearch(entry, searchCriteria)))
                .collect(toList());
    }

    @Override
    public void deleteById(I id) {
        getData().remove(getById(id));
    }

    protected <R extends JsonRelation<I>> Predicate<R> getPredicate(E e) {
        return null;
    }

    protected abstract <R extends JsonRelation<I>> I getId(R r);

    protected <T extends CommonModel<I, T>, R extends JsonRelation<I>> List<T> searchAtta(Class<T> target, List<T> from, List<R> relation, E criteriaEntity) {
        return from
                .stream()
                .filter(entity -> searchRelation(relation, getPredicate(criteriaEntity))
                        .stream()
                        .anyMatch(id -> isMatchRelation(entity, criteriaEntity)))
                .collect(toList());

    }

    protected <R extends JsonRelation<I>> List<I> searchRelation(List<R> fromRelation, Predicate<R> p) {
        return fromRelation.stream()
                .filter(p)
                .map(this::getId)
                .collect(toList());

    }

    protected <T extends CommonModel<I, T>> boolean isMatchRelation(T entity, E criteriaEntity) {
        return Objects.equals(entity.getId(), criteriaEntity.getId());
    }

    private boolean getCriteriaForSearch(E e, Map.Entry<String, Object> searchCriteria) {
        try {
            return (searchCriteria.getValue() == null ||
                    Objects.equals(BeanUtils.getProperty(e, searchCriteria.getKey()), searchCriteria.getValue()));
        } catch (ReflectiveOperationException ex) {
            return false;
        }
    }
}
