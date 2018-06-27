package com.intexsoft.repository.jsonrepository;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.model.Library;
import com.intexsoft.repository.BookLibraryRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonDataHolder;
import com.intexsoft.repository.jsonrepository.holders.JsonRelation;
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
        jsonDataHolder.getJsonData().getBookLibraryIds().add(new JsonRelation<UUID>()
                .setLeftEntityId(bookLibrary.getId().getBookId())
                .setRightEntityId(bookLibrary.getId().getLibraryId()));
        return bookLibrary;
    }

    @Override
    public List<Book> searchBooks(UUID libraryId) {
        return jsonDataHolder
                .getJsonData()
                .getBooks()
                .stream()
                .filter(book ->
                        search(getConvertedId(), libraryId)
                                .stream()
                                .map(BookLibraryId::getBookId)
                                .anyMatch(id -> getCriteriaForSearchRelation(book, id)))
                .collect(toList());
    }

    @Override
    public List<Library> searchLibraries(UUID bookId) {
        return jsonDataHolder
                .getJsonData()
                .getLibraries()
                .stream()
                .filter(library ->
                        search(getConvertedId(), bookId)
                                .stream()
                                .map(BookLibraryId::getLibraryId)
                                .anyMatch(id -> getCriteriaForSearchRelation(library, id)))
                .collect(toList());
    }

    private List<BookLibraryId> getConvertedId() {
        return jsonDataHolder
                .getJsonData()
                .getBookLibraryIds()
                .stream()
                .map(s -> new BookLibraryId()
                        .setBookId(s.getLeftEntityId())
                        .setLibraryId(s.getRightEntityId()))
                .collect(toList());

    }

    private List<BookLibraryId> search(List<BookLibraryId> fromRelation, UUID id) {
        return fromRelation.stream()
                .filter(bl -> Objects.equals(bl.getBookId(), id) || Objects.equals(bl.getLibraryId(), id))
                .collect(toList());

    }

    @Override
    protected List<BookLibrary> getData() {
        return Collections.emptyList();
    }
}
