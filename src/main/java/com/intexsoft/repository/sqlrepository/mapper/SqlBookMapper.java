package com.intexsoft.repository.sqlrepository.mapper;

import com.intexsoft.model.Book;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql")
public class SqlBookMapper extends CommonMapper<Book, UUID> {

    @Override
    protected Book getModel() {
        return new Book();
    }

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        return super.mapRow(resultSet, i)
                .setName(resultSet.getString("book_name"))
                .setAuthor(resultSet.getString("book_author"))
                .setPublished(resultSet.getDate("book_published"))
                .setNumberPages(resultSet.getInt("book_number_pages"));
    }

    @Override
    protected String getIdColumnName() {
        return "book_id";
    }

    @Override
    protected String getVersionColumn() {
        return "book_version";
    }

    @Override
    protected UUID convertedId(String string) {
        return UUID.fromString(string);
    }
}



