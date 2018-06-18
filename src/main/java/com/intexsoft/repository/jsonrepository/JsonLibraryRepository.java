package com.intexsoft.repository.jsonrepository;

import com.intexsoft.model.Library;
import com.intexsoft.repository.LibraryRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonDataHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "local", matchIfMissing = false)
public class JsonLibraryRepository extends JsonCommonRepository<Library, UUID> implements LibraryRepository {


    @Autowired
    private JsonDataHolder jsonDataHolder;

    @Override
    public List<Library> searchLibrary(String name, String address) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        searchCriterias.put("address", address);
        return search(searchCriterias);
    }

    @Override
    protected List<Library> getData() {
        return jsonDataHolder.getJsonData().getLibraryList();
    }
}
