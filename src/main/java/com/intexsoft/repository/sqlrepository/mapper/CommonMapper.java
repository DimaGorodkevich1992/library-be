package com.intexsoft.repository.sqlrepository.mapper;

import com.intexsoft.model.CommonModel;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CommonMapper<E extends CommonModel<I, E>, I extends Serializable> implements RowMapper<E> {

    protected abstract E getModel();

    protected abstract String getIdColumnName();

    protected abstract String getVersionColumn();

    protected abstract I convertedId(String string);

    @Override
    public E mapRow(ResultSet resultSet, int i) throws SQLException {
        return getModel()
                .setId(convertedId(resultSet.getString(getIdColumnName())))
                .setVersion(resultSet.getLong(getVersionColumn()));
    }
}


