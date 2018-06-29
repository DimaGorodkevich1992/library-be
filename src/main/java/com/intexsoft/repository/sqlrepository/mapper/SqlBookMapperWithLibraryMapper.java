package com.intexsoft.repository.sqlrepository.mapper;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SqlBookMapperWithLibraryMapper extends SqlBookMapper {

    private SqlBookLibraryMapper bookLibraryMapper;

    public SqlBookMapperWithLibraryMapper(SqlBookLibraryMapper bookLibraryMapper) {
        this.bookLibraryMapper = bookLibraryMapper;
    }

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        Set<BookLibrary> bookLibraries = new HashSet<>();
        bookLibraries.add(bookLibraryMapper.mapRow(resultSet, i));
        return super.mapRow(resultSet, i).setLibraries(bookLibraries);
    }
}
