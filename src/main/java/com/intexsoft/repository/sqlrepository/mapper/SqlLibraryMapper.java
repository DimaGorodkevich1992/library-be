package com.intexsoft.repository.sqlrepository.mapper;

import com.intexsoft.model.Library;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql")
public class SqlLibraryMapper extends CommonMapper<Library, UUID> {

    @Override
    public Library mapRow(ResultSet resultSet, int i) throws SQLException {
        return super.mapRow(resultSet, i)
                .setName(resultSet.getString("library_name"))
                .setAddress(resultSet.getString("library_address"));
    }

    @Override
    protected Library getModel() {
        return new Library();
    }

    @Override
    protected String getIdColumnName() {
        return "library_id";
    }

    @Override
    protected String getVersionColumn() {
        return "library_version";
    }

    @Override
    protected UUID convertedId(String string) {
        return Objects.equals(string, null) ? null : UUID.fromString(string);
    }

}
