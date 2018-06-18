package com.intexsoft.repository.jparepository;

import com.intexsoft.model.Library;
import com.intexsoft.repository.LibraryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbJpa", matchIfMissing = false)
public class JpaLibraryRepository extends JpaCommonRepository<Library, UUID> implements LibraryRepository {

    protected Class<Library> getModelClass() {
        return Library.class;
    }

    @Override
    public List<Library> searchLibrary(String name, String address) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        return search(searchCriterias);
    }
}
