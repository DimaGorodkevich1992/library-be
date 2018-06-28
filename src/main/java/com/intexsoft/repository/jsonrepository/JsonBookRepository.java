package com.intexsoft.repository.jsonrepository;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.repository.BookRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonData;
import com.intexsoft.repository.jsonrepository.holders.JsonDataHolder;
import com.intexsoft.repository.jsonrepository.holders.JsonRelationID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "local")
public class JsonBookRepository extends JsonCommonRepository<Book, UUID> implements BookRepository {

    @Autowired
    private JsonDataHolder jsonDataHolder;

    @Override
    protected <R extends JsonRelationID<UUID, UUID>> Predicate<R> getPredicate(Book book) {
        return p -> p.getLeftEntityId().equals(book.getId());
    }

    @Override
    protected <R extends JsonRelationID<UUID, UUID>> UUID getId(R r) {
        return r.getRightEntityId();
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
        Book book = getById(id);
        JsonData jsonData = jsonDataHolder.getJsonData();
        Set<BookLibrary> bookLibraries = searchItems(jsonData.getLibraries(), jsonData.getBookLibraryIds(), book)
                .stream()
                .map(library -> new BookLibrary()
                        .setId(new BookLibraryId().setBookId(id).setLibraryId(library.getId()))
                        .setBook(book)
                        .setLibrary(library))
                .collect(toSet());
        return book.setLibraries(bookLibraries);
    }

    @Override
    public List<Book> searchBook(String name, String author) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        searchCriterias.put("author", author);
        return search(searchCriterias);
    }

}