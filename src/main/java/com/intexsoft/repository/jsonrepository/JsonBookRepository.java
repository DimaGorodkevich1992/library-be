package com.intexsoft.repository.jsonrepository;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.model.CommonModel;
import com.intexsoft.model.Library;
import com.intexsoft.repository.BookRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonDataHolder;
import com.intexsoft.repository.jsonrepository.holders.JsonRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "local")
public class JsonBookRepository extends JsonCommonRepository<Book, UUID> implements BookRepository {

    @Autowired
    private JsonDataHolder jsonDataHolder;

    @Override
    protected List<JsonRelation<UUID>> getRelation() {
        return null;
    }

    @Override
    public UUID getGeneratedId(Book book) {
        return UUID.randomUUID();
    }

    @Override
    protected List<Book> getData() {
        return jsonDataHolder.getJsonData().getBooks();
    }

    @Override
    public Book getByIdWithLibraries(UUID id) {
       /* List<Library> libraries =  jsonDataHolder
                .getJsonData()
                .getLibraries()
                .stream()
                .filter(library ->
                        searchRelation(new jsonDataHolder.getJsonData().getBookLibraryIds(), id)
                                .stream()
                                .map(BookLibraryId::getLibraryId)
                                .anyMatch(uuid -> getCriteria(library, uuid)))
                .collect(Collectors.toList());*/
         return getById(id);
    }

    private List<BookLibraryId> searchRelation(List<BookLibraryId> fromRelation, UUID id) {
        return fromRelation.stream()
                .filter(bl -> Objects.equals(bl.getBookId(), id) || Objects.equals(bl.getLibraryId(), id))
                .collect(Collectors.toList());

    }

    private <T extends CommonModel> boolean getCriteria(T entity, UUID uuid) {
        return Objects.equals(entity.getId(), uuid);
    }

    @Override
    public List<Book> searchBook(String name, String author) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        searchCriterias.put("author", author);
        return search(searchCriterias);
    }

}