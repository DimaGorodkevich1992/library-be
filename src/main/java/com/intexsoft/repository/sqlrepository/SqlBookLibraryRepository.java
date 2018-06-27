package com.intexsoft.repository.sqlrepository;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.model.Library;
import com.intexsoft.repository.BookLibraryRepository;

import java.util.List;
import java.util.UUID;

public class SqlBookLibraryRepository extends SqlCommonRepository<BookLibrary,BookLibraryId> implements BookLibraryRepository {

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
        return null;
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
