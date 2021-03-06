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
        return Objects.equals(library.getId(), null)
                ? UUID.randomUUID()
                : library.getId();
    }

    protected Class<Library> getModelClass() {
        return Library.class;
    }

    @Override
    public Library getByIdWithBooks(UUID id) {
        return getById(id, Collections.singletonList("books,book"));
    }

    @Override
    public List<Library> searchLibrary(String name, String address) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        return search(searchCriterias);
    }
}
