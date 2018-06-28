package com.intexsoft.repository.sqlrepository;

import com.intexsoft.model.Library;
import com.intexsoft.repository.LibraryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql")
public class SqlLibraryRepository extends SqlCommonRepository<Library, UUID> implements LibraryRepository {

    private static final String SQL_SELECT_WITH_MAPPING =
            "SELECT " +
                    "libraries.id AS library_id, " +
                    "libraries.name AS library_name, " +
                    "libraries.address AS library_address, " +
                    "libraries.version AS library_version," +
                    "books.id AS book_id, " +
                    "books.name AS book_name, " +
                    "books.published AS book_published, " +
                    "books.author AS book_author, " +
                    "books.number_pages AS book_number_pages, " +
                    "books.version AS book_version";

    @Override
    public Library getByIdWithBooks(UUID id) {
        return getById(id, sqlGetByIdWithItems());
    }

    @Override
    protected String sqlGetByIdWithItems() {
        return SQL_SELECT_WITH_MAPPING + " " +
                "FROM libraries, books " +
                "INNER JOIN books_libraries AS B ON B.book_id = book.id" +
                "WHERE libraries_id = :id";
    }

    @Override
    protected String sqlGetById() {
        return "SELECT * FROM library WHERE library.id = :id";
    }

    @Override
    protected String sqlSave() {
        return "INSERT INTO library (id, name, address, version)VALUES (:id, :name, :address, :version)";
    }

    @Override
    protected String sqlSearch() {
        return "SELECT library.id AS library_id, library.name AS library_name, library.address, library.version AS library_version FROM library WHERE name = COALESCE(:name,name) and address = COALESCE(:address,address)";
    }

    @Override
    protected String sqlUpdate() {
        return "UPDATE library SET id = :id, name = :name, address = :address, version = :version WHERE id = :id";
    }

    @Override
    protected String sqlDelete() {
        return "DELETE FROM library WHERE id = :id";
    }

    @Override
    protected MapSqlParameterSource getCommonParametersSource(Library library) {
        return super.getCommonParametersSource(library)
                .addValue("name", library.getName())
                .addValue("address", library.getAddress());
    }

    @Override
    public List<Library> searchLibrary(String name, String address) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        searchCriterias.put("address", address);
        return search(searchCriterias);
    }
}
