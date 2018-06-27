package com.intexsoft.repository.sqlrepository;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.model.Library;
import com.intexsoft.repository.BookLibraryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql")
public class SqlBookLibraryRepository extends SqlCommonRepository<BookLibrary, BookLibraryId> implements BookLibraryRepository {

    @Override
    public List<Book> searchBooks(UUID libraryId) {
        return null;
    }

    @Override
    public List<Library> searchLibraries(UUID bookId) {
        return null;
    }

    @Override
    protected String sqlGetById() {
        return null;
    }

    @Override
    protected String sqlSave() {
        return "INSERT INTO books_libraries (book_id, library_id, version) VALUES (:book_id, :libraries_id, :version)";
    }

    @Override
    protected String sqlSearch() {
        return null;
    }

    @Override
    protected String sqlUpdate() {
        return null;
    }

    @Override
    protected String sqlDelete() {
        return null;
    }
}
