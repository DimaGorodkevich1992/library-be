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

import static java.util.stream.Collectors.toList;

@Slf4j
public abstract class JsonCommonRepository<E extends CommonModel<I, E>, I extends Serializable> implements CommonRepository<E, I> {

    protected abstract List<E> getData();

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
                org.springframework.beans.BeanUtils.copyProperties(e, var);
                var.setVersion(var.getVersion() + 1);
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

    protected <RE extends CommonModel<RI, RE>, RI extends Serializable> List<RE> findRightRelatedEntities(
            E entity, List<JsonRelationID<I, RI>> relations, List<RE> relatedEntities) {
        return relations.stream()
                .filter(relation -> relation.getLeftEntityId().equals(entity.getId()))
                .flatMap(relation -> relatedEntities.stream()
                        .filter(relatedEntity -> relatedEntity.getId().equals(relation.getRightEntityId())))
                .collect(toList());
    }

    protected <RE extends CommonModel<RI, RE>, RI extends Serializable> List<RE> findLeftRelatedEntities(
            E entity, List<JsonRelationID<RI, I>> relations, List<RE> relatedEntities) {
        return relations.stream()
                .filter(relation -> relation.getRightEntityId().equals(entity.getId()))
                .flatMap(relation -> relatedEntities.stream()
                        .filter(relatedEntity -> relatedEntity.getId().equals(relation.getLeftEntityId())))
                .collect(toList());
    }
}
