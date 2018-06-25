package com.intexsoft.repository.jsonrepository;

import com.intexsoft.model.Book;
import com.intexsoft.model.Library;
import com.intexsoft.repository.BookRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonDataHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "local")
public class JsonBookRepository extends JsonCommonRepository<Book, UUID> implements BookRepository {

    @Override
    public UUID getGeneratedId(Book book) {
        return UUID.randomUUID();
    }

    @Autowired
    private JsonDataHolder jsonDataHolder;

    @Override
    protected void getEntity(Book book) {
    }

    @Override
    public Book getByIdWithLibraries(UUID id) {
        List<UUID> librariesId = jsonDataHolder
                .getJsonData()
                .getBookLibraries()
                .stream()
                .filter(bl -> Objects.equals(bl.getBook().getId(), id))
                .map(bl -> bl.getLibrary().getId())
                .collect(Collectors.toList());
        Set<Library> libraries = jsonDataHolder
                .getJsonData()
                .getLibraries()
                .stream()
                .filter(library -> librariesId
                        .stream()
                        .anyMatch(libId -> getCriteria(library, id)))
                .collect(Collectors.toSet());

      //  return getById(id).setLibraries(libraries);
        return null;
    }

    private boolean getCriteria(Library library, UUID uuid) {
        return Objects.equals(library.getId(), uuid);
    }

    @Override
    protected List<Book> getData() {
        return jsonDataHolder.getJsonData().getBooks();
    }

    @Override
    public List<Book> searchBook(String name, String author) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        searchCriterias.put("author", author);
        return search(searchCriterias);
    }

}