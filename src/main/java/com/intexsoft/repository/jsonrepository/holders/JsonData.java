package com.intexsoft.repository.jsonrepository.holders;

import com.intexsoft.model.Book;
import com.intexsoft.model.Library;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class JsonData {

    private List<Book> books;
    private List<Library> libraries;
    private List<JsonRelationID<UUID,UUID>> bookLibraryIds;
}
