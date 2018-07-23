package com.intexsoft.repository.jsonrepository;

import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.repository.BookLibraryRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonData;
import com.intexsoft.repository.jsonrepository.holders.JsonDataHolder;
import com.intexsoft.repository.jsonrepository.holders.JsonRelationID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "local")
public class JsonBookLibraryRepository extends JsonCommonRepository<BookLibrary, BookLibraryId> implements BookLibraryRepository {

    @Autowired
    private JsonDataHolder jsonDataHolder;

    @Override
    protected List<BookLibrary> getData() {
        return Collections.emptyList();
    }

    @Override
    public BookLibrary save(BookLibrary bookLibrary) {
        JsonData jsonData = jsonDataHolder.getJsonData();
        boolean isExistBook = isExist(jsonData.getBooks(), s -> Objects.equals(s.getId(), bookLibrary.getId().getBookId()));
        boolean isExistLibrary = isExist(jsonData.getLibraries(), s -> Objects.equals(s.getId(), bookLibrary.getId().getLibraryId()));
        boolean isExistRelation = isExist(jsonData.getBookLibraryIds(),
                s -> Objects.equals(s.getLeftEntityId(), bookLibrary.getId().getBookId())
                && Objects.equals(s.getRightEntityId(), bookLibrary.getId().getLibraryId()));
        if (isExistRelation) {
            throw new DataIntegrityViolationException("Duplicate relation entity");
        } else {
            if (isExistBook && isExistLibrary) {
                jsonDataHolder.getJsonData().getBookLibraryIds().add(new JsonRelationID<UUID, UUID>()
                        .setLeftEntityId(bookLibrary.getId().getBookId())
                        .setRightEntityId(bookLibrary.getId().getLibraryId()));
                return bookLibrary;
            } else {
                throw new DataIntegrityViolationException("Entity not present");
            }
        }

    }

    private <E> boolean isExist(Collection<E> collectionForSearch, Predicate<E> predicate) {
        return collectionForSearch
                .stream()
                .anyMatch(predicate);
    }

}