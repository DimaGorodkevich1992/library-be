package com.intexsoft.repository.sqlrepository.mapper;

import com.intexsoft.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql")
public class SqlBookWithLibrariesMapper extends SqlBookMapper {

    @Autowired
    private SqlBookLibraryMapper bookLibraryMapper;

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        return Objects.equals(resultSet.getString("library_id"), null)
                ? super.mapRow(resultSet, i).setLibraries(Collections.emptySet())
                : super.mapRow(resultSet, i).setLibraries(Collections.singleton(bookLibraryMapper.mapRow(resultSet, i)));
    }
}
