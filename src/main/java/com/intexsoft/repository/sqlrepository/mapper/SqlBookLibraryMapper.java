package com.intexsoft.repository.sqlrepository.mapper;

import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SqlBookLibraryMapper extends CommonMapper<BookLibrary, BookLibraryId> {

    private SqlBookMapper bookMapper;

    private SqlLibraryMapper libraryMapper;

    public SqlBookLibraryMapper(SqlBookMapper bookMapper, SqlLibraryMapper libraryMapper) {
        this.bookMapper = bookMapper;
        this.libraryMapper = libraryMapper;
    }

    @Override
    protected BookLibrary getModel() {
        return new BookLibrary();
    }

    @Override
    public BookLibrary mapRow(ResultSet resultSet, int i) throws SQLException {
        return new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(UUID.fromString(resultSet.getString("book_id")))
                        .setLibraryId(UUID.fromString(resultSet.getString("library_id"))))
                .setBook(bookMapper.mapRow(resultSet, i))
                .setLibrary(libraryMapper.mapRow(resultSet, i))
                .setVersion(resultSet.getLong("book_library_version"));
    }

    @Override
    protected String getIdColumnName() {
        return "book_id";
    }

    @Override
    protected String getVersionColumn() {
        return "book_library_id";
    }

    @Override
    protected BookLibraryId convertedId(String string) {
        return new BookLibraryId();
    }
}
