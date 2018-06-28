package com.intexsoft.repository.sqlrepository.mapper;

import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql")
public class SqlBookLibraryMapper extends CommonMapper<BookLibrary, BookLibraryId> {

    @Autowired
    private SqlBookMapper bookMapper;
    @Autowired
    private SqlLibraryMapper libraryMapper;

    @Override
    protected BookLibrary getModel() {
        return new BookLibrary();
    }

    @Override
    public BookLibrary mapRow(ResultSet resultSet, int i) throws SQLException {
        return super.mapRow(resultSet, i);
    }

    @Override
    protected String getIdColumnName() {
        return null;
    }

    @Override
    protected String getVersionColumn() {
        return null;
    }

    @Override
    protected BookLibraryId convertedId(String string) {
        return null;
    }
}
