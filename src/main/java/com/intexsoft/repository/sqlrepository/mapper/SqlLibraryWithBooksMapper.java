package com.intexsoft.repository.sqlrepository.mapper;

import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql")
public class SqlLibraryWithBooksMapper extends SqlLibraryMapper {

    @Autowired
    private SqlBookLibraryMapper bookLibraryMapper;

    @Override
    public Library mapRow(ResultSet resultSet, int i) throws SQLException {
        Set<BookLibrary> bookLibraries = new HashSet<>();
        bookLibraries.add(bookLibraryMapper.mapRow(resultSet, i));
        return super.mapRow(resultSet, i).setBooks(bookLibraries);
    }
}
