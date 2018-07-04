package com.intexsoft.repository.sqlrepository.mapper;

import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql")
public class SqlBookLibraryMapper extends CommonMapper<BookLibrary, BookLibraryId> {

    @Autowired
    @Qualifier("sqlBookMapper")
    private SqlBookMapper bookMapper;
    @Autowired
    @Qualifier("sqlLibraryMapper")
    private SqlLibraryMapper libraryMapper;

    @Override
    protected BookLibrary getModel() {
        return new BookLibrary();
    }

    @Override
    public BookLibrary mapRow(ResultSet resultSet, int i) throws SQLException {
        if (Objects.isNull(resultSet.getString("book_id"))
                || Objects.isNull(resultSet.getString("library_id"))) {
            return null;
        } else {
            return new BookLibrary()
                    .setId(new BookLibraryId()
                            .setBookId(UUID.fromString(resultSet.getString("book_id")))
                            .setLibraryId(UUID.fromString(resultSet.getString("library_id"))))
                    .setBook(bookMapper.mapRow(resultSet, i))
                    .setLibrary(libraryMapper.mapRow(resultSet, i))
                    .setVersion(resultSet.getLong("book_library_version"));

        }

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
