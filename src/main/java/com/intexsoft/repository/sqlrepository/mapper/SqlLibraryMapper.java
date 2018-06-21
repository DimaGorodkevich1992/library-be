package com.intexsoft.repository.sqlrepository.mapper;

import com.intexsoft.model.Library;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Slf4j
@Component
public class SqlLibraryMapper extends CommonMapper<Library, UUID> {


    @Override
    public Library mapRow(ResultSet resultSet, int i) throws SQLException {
        return super.mapRow(resultSet, i)
                .setName(resultSet.getString("libraries_name"))
                .setAddress(resultSet.getString("address"));
    }

    @Override
    protected Library getModel() {
        return new Library();
    }

    @Override
    protected String getIdColumnName() {
        return "libraries_id";
    }

    @Override
    protected String getVersionColumn() {
        return "libraries_version";
    }

    @Override
    protected UUID convertedId(String string) {
        return UUID.fromString(string);
    }

}
