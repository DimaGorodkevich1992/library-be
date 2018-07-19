package com.intexsoft.repository.jsonrepository;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.repository.BookRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonData;
import com.intexsoft.repository.jsonrepository.holders.JsonDataHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.stream.Collectors.toSet;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "local")
public class JsonBookRepository extends JsonCommonRepository<Book, UUID> implements BookRepository {

    @Autowired
    private JsonDataHolder jsonDataHolder;

    @Override
    public UUID getGeneratedId(Book book) {
        return Objects.equals(book.getId(), null)
                ? UUID.randomUUID()
                : book.getId();
    }

    @Override
    protected List<Book> getData() {
        return jsonDataHolder.getJsonData().getBooks();
    }

    @Override
    public Book getByIdWithLibraries(UUID id) {
        Book book = getById(id);
        if (Objects.equals(book, null)) {
            return book;
        } else {
            JsonData jsonData = jsonDataHolder.getJsonData();
            Set<BookLibrary> bookLibraries = findRightRelatedEntities(book, jsonData.getBookLibraryIds(), jsonData.getLibraries())
                    .stream()
                    .map(library -> new BookLibrary()
                            .setId(new BookLibraryId().setBookId(id).setLibraryId(library.getId()))
                            .setBook(book)
                            .setLibrary(library))
                    .collect(toSet());
            return book.setLibraries(bookLibraries);
        }
    }

    @Override
    public List<Book> searchBook(String name, String author) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        searchCriterias.put("author", author);
        return search(searchCriterias);
    }

}