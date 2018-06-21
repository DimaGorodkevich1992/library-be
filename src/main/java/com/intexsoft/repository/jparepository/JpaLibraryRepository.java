package com.intexsoft.repository.jparepository;

import com.intexsoft.model.Library;
import com.intexsoft.repository.LibraryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbJpa")
public class JpaLibraryRepository extends JpaCommonRepository<Library, UUID> implements LibraryRepository {

    @Override
    public UUID getGeneratedId(Library library) {
        return UUID.randomUUID();
    }

    protected Class<Library> getModelClass() {
        return Library.class;
    }

    @Override
    public Library getByIdWithBooks(UUID id) {
        List<String> fetchCriterais = new ArrayList<>();
        fetchCriterais.add(0, "books");
        fetchCriterais.add(1, "book");
        return getById(id, fetchCriterais);
    }

    @Override
    public List<Library> searchLibrary(String name, String address) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        return search(searchCriterias);
    }
}
