package com.intexsoft.controller.dtomapper;


import com.intexsoft.dto.CommonDto;
import com.intexsoft.model.CommonModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
public abstract class AbstractDtoMapper<E extends CommonModel<I>, D extends CommonDto<I>, I> {

    protected abstract Class<E> getEntityClass();

    protected abstract Class<D> getDtoClass();


    public final D toDto(E entity) {
        D dto = map(entity, getDtoClass());
        instructionToDto(entity, dto);
        return dto;
    }

    public final Set<D> toDto(Set<E> entities) {
        return entities
                .stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    public final Set<E> fromDto(Set<D> dtos) {
        return dtos
                .stream()
                .map(this::fromDto)
                .collect(Collectors.toSet());
    }

    public final E fromDto(D dto) {
        E entity = map(dto, getEntityClass());
        instructionToEntity(dto, entity);
        return entity;
    }

    public final List<D> toDto(List<E> entity) {
        return entity
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public final List<E> fromDto(List<D> dto) {
        return dto
                .stream()
                .map(this::fromDto)
                .collect(Collectors.toList());
    }

    protected void instructionToDto(E entity, D dto) {

    }

    protected void instructionToEntity(D dto, E entity) {

    }

    protected final <S, T> T map(S source, Class<T> targetClass) {
        T var = null;
        try {
            var = targetClass.newInstance();
        } catch (ReflectiveOperationException e) {
            log.error("Failed instantiate", e);
            throw new RuntimeException("Failed instantiate ", e);
        }
        BeanUtils.copyProperties(source, var);
        return var;
    }
}
