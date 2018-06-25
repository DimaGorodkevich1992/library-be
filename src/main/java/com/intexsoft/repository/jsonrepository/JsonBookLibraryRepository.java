package com.intexsoft.repository.jsonrepository;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.model.Library;
import com.intexsoft.repository.BookLibraryRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonDataHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JsonBookLibraryRepository extends JsonCommonRepository<BookLibrary, BookLibraryId> implements BookLibraryRepository {

    @Override
    protected void getEntity(BookLibrary bookLibrary) {
    }

    @Autowired
    private JsonDataHolder jsonDataHolder;

    @Override
    public List<Book> searchBooks(UUID libraryId) {
        return null;
    }

    @Override
    public List<Library> searchLibraries(UUID bookId) {
        return null;
    }

    @Override
    protected List<BookLibrary> getData() {
        return jsonDataHolder.getJsonData().getBookLibraries();
    }
}
