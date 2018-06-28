package com.intexsoft.repository.jsonrepository;

import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.repository.BookLibraryRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonDataHolder;
import com.intexsoft.repository.jsonrepository.holders.JsonRelationID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
        jsonDataHolder.getJsonData().getBookLibraryIds().add(new JsonRelationID<UUID, UUID>()
                .setLeftEntityId(bookLibrary.getId().getBookId())
                .setRightEntityId(bookLibrary.getId().getLibraryId()));
        return bookLibrary;
    }
}
