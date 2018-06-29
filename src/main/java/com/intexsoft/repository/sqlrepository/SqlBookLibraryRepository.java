package com.intexsoft.repository.sqlrepository;

import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.repository.BookLibraryRepository;
import com.intexsoft.repository.sqlrepository.mapper.CommonMapper;
import com.intexsoft.repository.sqlrepository.mapper.SqlBookLibraryMapper;
import com.intexsoft.repository.sqlrepository.mapper.SqlBookMapper;
import com.intexsoft.repository.sqlrepository.mapper.SqlLibraryMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql")
public class SqlBookLibraryRepository extends SqlCommonRepository<BookLibrary, BookLibraryId> implements BookLibraryRepository {



    public SqlBookLibraryRepository() {
        super(new SqlBookLibraryMapper(new SqlBookMapper(),new SqlLibraryMapper()));
    }

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
        return "INSERT INTO books_libraries (book_id, library_id, version) VALUES (:book_id, :libraries_id, :version)";
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
}
