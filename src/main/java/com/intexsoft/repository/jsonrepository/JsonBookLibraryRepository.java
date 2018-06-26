package com.intexsoft.repository.jsonrepository;

import com.intexsoft.model.*;
import com.intexsoft.repository.BookLibraryRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonDataHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "local")
public class JsonBookLibraryRepository extends JsonCommonRepository<BookLibrary, BookLibraryId> implements BookLibraryRepository {

    @Autowired
    private JsonDataHolder jsonDataHolder;

    @Override
    public BookLibrary save(BookLibrary bookLibrary) {
        jsonDataHolder.getJsonData().getBookLibraryIds().add(bookLibrary.getId());
        return bookLibrary;
    }

    @Override
    public List<Book> searchBooks(UUID libraryId) {
        return jsonDataHolder
                .getJsonData()
                .getBooks()
                .stream()
                .filter(book ->
                        searchRelation(jsonDataHolder.getJsonData().getBookLibraryIds(), libraryId)
                                .stream()
                                .map(BookLibraryId::getBookId)
                                .anyMatch(id -> getCriteria(book, id)))
                .collect(toList());
    }

    @Override
    public List<Library> searchLibraries(UUID bookId) {
        return jsonDataHolder
                .getJsonData()
                .getLibraries()
                .stream()
                .filter(library ->
                        searchRelation(jsonDataHolder.getJsonData().getBookLibraryIds(), bookId)
                                .stream()
                                .map(BookLibraryId::getLibraryId)
                                .anyMatch(id -> getCriteria(library, id)))
                .collect(toList());
    }

    private List<BookLibraryId> searchRelation(List<BookLibraryId> fromRelation, UUID id) {
        return fromRelation.stream()
                .filter(bl -> Objects.equals(bl.getBookId(), id) || Objects.equals(bl.getLibraryId(), id))
                .collect(toList());

    }

    private <T extends CommonModel> boolean getCriteria(T entity, UUID uuid) {
        return Objects.equals(entity.getId(), uuid);
    }

    @Override
    protected List<BookLibrary> getData() {
        return Collections.emptyList();
    }
}
