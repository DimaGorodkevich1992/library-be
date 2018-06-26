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
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
public abstract class JsonCommonRepository<E extends CommonModel<I, E>, I extends Serializable> implements CommonRepository<E, I> {

    @Override
    public I getGeneratedId(E e) {
        return e.getId();
    }

    protected abstract List<E> getData();

    protected abstract List<JsonRelation<I>> getRelation();

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
                        .allMatch(searchCriteria -> getCriteria(entry, searchCriteria)))
                .collect(toList());
    }

    @Override
    public void deleteById(I id) {
        getData().remove(getById(id));
    }

    private List<JsonRelation<I>> searchRelation(List<JsonRelation<I>> fromRelation, UUID id) {
        return fromRelation.stream()
                .filter(r -> Objects.equals(r.getIdLeft(), id) || Objects.equals(r.getIdRight(), id))
                .collect(toList());

    }

    private boolean getCriteria(E e, Map.Entry<String, Object> searchCriteria) {
        try {
            return (searchCriteria.getValue() == null ||
                    Objects.equals(BeanUtils.getProperty(e, searchCriteria.getKey()), searchCriteria.getValue()));
        } catch (ReflectiveOperationException ex) {
            return false;
        }
    }
}
