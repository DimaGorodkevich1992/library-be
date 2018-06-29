package com.intexsoft.repository.sqlrepository.mapper;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
@Component
public class SqlBookMapperWithLibraryMapper extends SqlBookMapper {
    @Autowired
    private SqlBookLibraryMapper bookLibraryMapper;

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        Set<BookLibrary> bookLibraries = new HashSet<>();
        bookLibraries.add(bookLibraryMapper.mapRow(resultSet, i));
        return super.mapRow(resultSet, i).setLibraries(bookLibraries);
    }
}
