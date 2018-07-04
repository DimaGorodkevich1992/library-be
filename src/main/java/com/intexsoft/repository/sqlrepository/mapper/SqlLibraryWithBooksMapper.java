package com.intexsoft.repository.sqlrepository.mapper;

import com.intexsoft.model.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql")
public class SqlLibraryWithBooksMapper extends SqlLibraryMapper {

    @Autowired
    private SqlBookLibraryMapper bookLibraryMapper;

    @Override
    public Library mapRow(ResultSet resultSet, int i) throws SQLException {
        return Objects.equals(resultSet.getString("book_id"), null)
                ? super.mapRow(resultSet, i).setBooks(Collections.emptySet())
                : super.mapRow(resultSet, i).setBooks(Collections.singleton(bookLibraryMapper.mapRow(resultSet, i)));
    }
}
