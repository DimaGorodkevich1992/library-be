/*
package com.intexsoft.repository.sqlrepository.mapper;

import com.intexsoft.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Slf4j
@Component
public class SqlBookMapper extends CommonMapper<Book, UUID> {

    @Autowired
    private SqlLibraryMapper libraryMapper;

    @Override
    protected Book getModel() {
        return new Book();
    }

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        return super.mapRow(resultSet, i)
                .setName(resultSet.getString("books_name"))
                .setAuthor(resultSet.getString("author"))
                .setPublished(resultSet.getDate("published"))
                .setNumberPages(resultSet.getInt("number_pages"))
                .setLibrary(libraryMapper.mapRow(resultSet, i));
    }

    @Override
    protected String getIdColumnName() {
        return "books_id";
    }

    @Override
    protected String getVersionColumn() {
        return "books_version";
    }

    @Override
    protected UUID convertedId(String string) {
        return UUID.fromString(string);
    }
}



*/
