package com.intexsoft.repository.sqlrepository;

import com.intexsoft.model.CommonModel;
import com.intexsoft.repository.CommonRepository;
import com.intexsoft.repository.sqlrepository.mapper.CommonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
public abstract class SqlCommonRepository<E extends CommonModel<I>, I> implements CommonRepository<E, I> {

    @Autowired
    private CommonMapper<E, I> mapper;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

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
    public E getById(I id) {
        return jdbcTemplate.query(sqlGetById(), new MapSqlParameterSource("id", id), mapper)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public E save(E e) {
        jdbcTemplate.update(sqlSave(), getCommonParametersSource(e));
        return getById(e.getId());
    }

    @Override
    public List<E> search(Map<String, Object> searchCriterias) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(searchCriterias);
        return jdbcTemplate.query(sqlSearch(), sqlParameterSource, mapper);
    }

    @Transactional
    @Override
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
        return var;
    }


    @Override
    public void deleteById(I id) {
        jdbcTemplate.update(sqlDelete(), new MapSqlParameterSource("id", id));
    }
}
