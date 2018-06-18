package com.intexsoft.repository;

import com.intexsoft.model.CommonModel;

import java.util.List;
import java.util.Map;

public interface CommonRepository<E extends CommonModel<I>,I> {
    E getById(I id);

    E save(E e);

    List<E> search(Map<String, Object> searchCriterias);

    E update(E e);

    void deleteById(I id);

}
