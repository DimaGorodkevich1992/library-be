package com.intexsoft.repository.jsonrepository;

import com.intexsoft.exception.DuplicateNameException;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.model.Library;
import com.intexsoft.repository.LibraryRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonData;
import com.intexsoft.repository.jsonrepository.holders.JsonDataHolder;
import com.intexsoft.repository.jsonrepository.holders.JsonRelationID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "local")
public class JsonLibraryRepository extends JsonCommonRepository<Library, UUID> implements LibraryRepository {

    @Autowired
    private JsonDataHolder jsonDataHolder;

    @Override
    public Library save(Library library) {
        if (search(Collections.singletonMap("name", library.getName())).isEmpty()) {
            return super.save(library);
        } else {
            log.error("Name already exists: ", "save");
            throw new DuplicateNameException(" name already exists");
        }
    }

    @Override
    public UUID getGeneratedId(Library library) {
        return Objects.equals(library.getId(), null)
                ? UUID.randomUUID()
                : library.getId();
    }

    @Override
    protected List<Library> getData() {
        return jsonDataHolder.getJsonData().getLibraries();
    }

    @Override
    public Library getByIdWithBooks(UUID id) {
        Library library = getById(id);
        if (Objects.equals(library, null)) {
            return library;
        } else {
            JsonData jsonData = jsonDataHolder.getJsonData();
            Set<BookLibrary> bookLibraries = findLeftRelatedEntities(library, jsonData.getBookLibraryIds(), jsonData.getBooks())
                    .stream()
                    .map(book -> new BookLibrary()
                            .setId(new BookLibraryId().setBookId(book.getId()).setLibraryId(id))
                            .setBook(book)
                            .setLibrary(library))
                    .collect(toSet());
            return library.setBooks(bookLibraries);
        }

    }

    @Override
    public List<Library> searchLibrary(String name, String address) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        searchCriterias.put("address", address);
        return search(searchCriterias);
    }

    @Override
    public void deleteById(UUID id) {
        List<JsonRelationID> libraryIds = new ArrayList<>(jsonDataHolder.getJsonData().getBookLibraryIds());
        libraryIds.forEach(s -> {
            if (Objects.equals(s.getRightEntityId(), id)) {
                jsonDataHolder.getJsonData().getBookLibraryIds().remove(s);
            }
        });
        super.deleteById(id);
    }
}
