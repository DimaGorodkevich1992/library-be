package com.intexsoft.repository.jsonrepository;

import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.repository.BookLibraryRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonBookLibrary;
import com.intexsoft.repository.jsonrepository.holders.JsonDataHolder;
import com.intexsoft.repository.jsonrepository.holders.JsonRelationID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "local")
public class JsonBookLibraryRepository extends JsonCommonRepository<BookLibrary, BookLibraryId> implements BookLibraryRepository {

    @Autowired
    private JsonDataHolder jsonDataHolder;

    @Override
    protected <R extends JsonRelationID<BookLibraryId, BookLibraryId>> BookLibraryId getId(R r) {
        return null;
    }

    @Override
    protected List<BookLibrary> getData() {
        return Collections.emptyList();
    }

    @Override
    public BookLibrary save(BookLibrary bookLibrary) {
        jsonDataHolder.getJsonData().getBookLibraryIds().add((JsonBookLibrary) new JsonBookLibrary()
                .setLeftEntityId(bookLibrary.getId().getBookId())
                .setRightEntityId(bookLibrary.getId().getLibraryId()));
        return bookLibrary;
    }
}
