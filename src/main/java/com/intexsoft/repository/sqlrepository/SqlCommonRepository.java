package com.intexsoft.repository.sqlrepository;

import com.intexsoft.model.CommonModel;
import com.intexsoft.repository.CommonRepository;
import com.intexsoft.repository.sqlrepository.mapper.CommonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

@Slf4j
public abstract class SqlCommonRepository<E extends CommonModel<I, E>, I extends Serializable> implements CommonRepository<E, I> {

    private CommonMapper<E, I> mapper;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public SqlCommonRepository(CommonMapper<E, I> mapper) {
        this.mapper = mapper;
    }

    protected abstract String sqlGetById();

    protected abstract String sqlSave();

    protected abstract String sqlSearch();

    protected abstract String sqlUpdate();

    protected abstract String sqlDelete();

    protected MapSqlParameterSource getCommonParametersSource(E e) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", e.getId());
        mapSqlParameterSource.addValue("version", e.getVersion());
        return mapSqlParameterSource;
    }

    @Override
    public I getGeneratedId(E e) {
        return e.getId();
    }

    protected E getByIdWithJoins(I id, String sqlQuery, RowMapper<E> rowMapper, BinaryOperator<E> reduceOperator) {
        return jdbcTemplate.query(sqlQuery, new MapSqlParameterSource("id", id), rowMapper)
                .stream()
                .reduce(reduceOperator)
                .orElse(null);

    }

    @Override
    public E getById(I id) {
        return jdbcTemplate.query(sqlGetById(), new MapSqlParameterSource("id", id), mapper)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public E save(E e) {
        e.setId(getGeneratedId(e))
                .setVersion(1);
        jdbcTemplate.update(sqlSave(), getCommonParametersSource(e));
        return getById(e.getId());
    }

    @Override
    public List<E> search(Map<String, Object> searchCriterias) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(searchCriterias);
        return jdbcTemplate.query(sqlSearch(), sqlParameterSource, mapper);
    }


    @Override
    @Transactional
    public E update(E e) {
        E var = getById(e.getId());
        synchronized (var) {
            if (var.getVersion() == e.getVersion()) {
                e.setVersion(e.getVersion() + 1);
                jdbcTemplate.update(sqlUpdate(), getCommonParametersSource(e));
            } else {
                log.error("Version mismatch!!! you version " +
                        e.toString() + " last version " + var.toString());
                throw new IllegalArgumentException("version mismatch!!! you version " +
                        e.toString() + " last version " + var.toString());
            }
        }
        return e;
    }

    @Override
    public void deleteById(I id) {
        jdbcTemplate.update(sqlDelete(), new MapSqlParameterSource("id", id));
    }
}
