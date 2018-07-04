package com.intexsoft.repository.sqlrepository;

import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.repository.BookLibraryRepository;
import com.intexsoft.repository.sqlrepository.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql")
public class SqlBookLibraryRepository extends SqlCommonRepository<BookLibrary, BookLibraryId> implements BookLibraryRepository {

    public SqlBookLibraryRepository(CommonMapper<BookLibrary, BookLibraryId> mapper) {
        super(mapper);
    }

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    protected String sqlGetByIdWithItems() {
        return null;
    }

    @Override
    protected String sqlGetById() {
        return null;
    }

    @Override
    protected String sqlSave() {
        return "INSERT INTO books_libraries (book_id, library_id, version) VALUES (:book_id, :library_id, :version)";
    }

    @Override
    protected String sqlSearch() {
        return null;
    }

    @Override
    protected String sqlUpdate() {
        return null;
    }

    @Override
    protected String sqlDelete() {
        return null;
    }

    @Override
    public BookLibrary save(BookLibrary bookLibrary) {
        jdbcTemplate.update(sqlSave(), getCommonParametersSource(bookLibrary));
        return bookLibrary;
    }

    @Override
    protected MapSqlParameterSource getCommonParametersSource(BookLibrary bookLibrary) {
        return new MapSqlParameterSource()
                .addValue("version", bookLibrary.getVersion())
                .addValue("book_id", bookLibrary.getId().getBookId())
                .addValue("library_id", bookLibrary.getId().getLibraryId());
    }
}
