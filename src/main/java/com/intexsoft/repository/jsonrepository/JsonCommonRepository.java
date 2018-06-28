package com.intexsoft.repository.jsonrepository;

import com.intexsoft.model.CommonModel;
import com.intexsoft.repository.CommonRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonRelationID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
public abstract class JsonCommonRepository<E extends CommonModel<I, E>, I extends Serializable> implements CommonRepository<E, I> {

    protected <T extends CommonModel<ID, T>, F extends CommonModel<I, F>, ID extends Serializable> T getItem(T target, F from, E entity, ID targetId) {
        return target;
    }

    protected <T extends CommonModel<I, T>> T getModel() {
        return null;
    }

    protected abstract <R extends JsonRelationID<I, I>> I getId(R r);

    protected abstract List<E> getData();

    protected <R extends JsonRelationID<I, I>> Predicate<R> getPredicate(E e) {
        return null;
    }

    @Override
    public I getGeneratedId(E e) {
        return e.getId();
    }

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

    private boolean getCriteriaForSearch(E e, Map.Entry<String, Object> searchCriteria) {
        try {
            return (searchCriteria.getValue() == null ||
                    Objects.equals(BeanUtils.getProperty(e, searchCriteria.getKey()), searchCriteria.getValue()));
        } catch (ReflectiveOperationException ex) {
            return false;
        }
    }

    protected <T extends CommonModel<I, T>, R extends JsonRelationID<I, I>> List<T> searchItems(List<T> target, List<R> relation, E criteriaEntity) {
        return target
                .stream()
                .filter(entity -> isMathRelation(relation, getPredicate(criteriaEntity), criteriaEntity))
                .collect(toList());

    }

    protected <R extends JsonRelationID<I, I>> boolean isMathRelation(List<R> fromRelation, Predicate<R> predicate, E criteriaEntity) {
        return fromRelation.stream()
                .filter(predicate)
                .map(this::getId)
                .anyMatch(i -> Objects.equals(i, criteriaEntity.getId()));

    }

    protected <T extends CommonModel<ID, T>, F extends CommonModel<I, F>,ID extends Serializable > Set<T> getItemSet(T target, List<F> from, E entity, ID targetId) {
        return from
                .stream()
                .map(f -> getItem(target, f, entity,targetId))
                .collect(toSet());
    }

}
