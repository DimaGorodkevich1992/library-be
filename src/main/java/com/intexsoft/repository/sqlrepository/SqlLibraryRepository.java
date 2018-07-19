package com.intexsoft.repository.sqlrepository;

import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.Library;
import com.intexsoft.repository.LibraryRepository;
import com.intexsoft.repository.sqlrepository.mapper.CommonMapper;
import com.intexsoft.repository.sqlrepository.mapper.SqlLibraryWithBooksMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BinaryOperator;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql")
public class SqlLibraryRepository extends SqlCommonRepository<Library, UUID> implements LibraryRepository {

    @Autowired
    private SqlLibraryWithBooksMapper sqlLibraryWithBooksMapper;

    public SqlLibraryRepository(@Qualifier("sqlLibraryMapper") CommonMapper<Library, UUID> mapper) {
        super(mapper);
    }

    @Override
    public UUID getGeneratedId(Library library) {
        return Objects.equals(library.getId(), null)
                ? UUID.randomUUID()
                : library.getId();
    }

    private static final String SQL_SELECT =
            "SELECT " +
                    "L.id AS library_id, " +
                    "L.name AS library_name, " +
                    "L.address AS library_address, " +
                    "L.version AS library_version";

    private static final String SQL_SELECT_WITH_MAPPING =
            SQL_SELECT + ", " +
                    "B.id AS book_id, " +
                    "B.name AS book_name, " +
                    "B.published AS book_published, " +
                    "B.author AS book_author, " +
                    "B.number_pages AS book_number_pages, " +
                    "B.version AS book_version," +
                    "BL.version AS book_library_version";

    private String sqlGetByIdWithBooks() {
        return SQL_SELECT_WITH_MAPPING + " " +
                "FROM libraries AS L " +
                "LEFT JOIN books_libraries AS BL ON L.id = BL.library_id " +
                "LEFT JOIN books AS B ON B.id = BL.book_id " +
                "WHERE L.id = :id";
    }

    @Override
    protected String sqlGetById() {
        return SQL_SELECT + " " +
                "FROM libraries AS L" +
                " WHERE L.id = :id";
    }

    @Override
    protected String sqlSave() {
        return "INSERT INTO libraries (id, name, address, version)VALUES (:id, :name, :address, :version)";
    }

    @Override
    protected String sqlSearch() {
        return SQL_SELECT + " " +
                "FROM libraries AS L " +
                "WHERE L.name = COALESCE(:name,L.name) and L.address = COALESCE(:address,L.address)";
    }

    @Override
    protected String sqlUpdate() {
        return "UPDATE libraries SET id = :id, name = :name, address = :address, version = :version WHERE id = :id";
    }

    @Override
    protected String sqlDelete() {
        return "DELETE FROM libraries WHERE id = :id";
    }

    @Override
    public Library getByIdWithBooks(UUID id) {
        BinaryOperator<Library> reducedLibraries = (library, library2) -> {
            Set<BookLibrary> set = new HashSet<>();
            set.addAll(library.getBooks());
            set.addAll(library2.getBooks());
            library.setBooks(set);
            return library;
        };
        return getByIdWithJoins(id, sqlGetByIdWithBooks(), sqlLibraryWithBooksMapper, reducedLibraries);
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
