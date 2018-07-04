package com.intexsoft.repository.sqlrepository;

import com.intexsoft.model.Book;
import com.intexsoft.repository.BookRepository;
import com.intexsoft.repository.sqlrepository.mapper.CommonMapper;
import com.intexsoft.repository.sqlrepository.mapper.SqlBookWithLibrariesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BinaryOperator;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql")
public class SqlBookRepository extends SqlCommonRepository<Book, UUID> implements BookRepository {

    @Autowired
    private SqlBookWithLibrariesMapper sqlBookWithLibrariesMapper;

    public SqlBookRepository(@Qualifier("sqlBookMapper") CommonMapper<Book, UUID> mapper) {
        super(mapper);
    }

    @Override
    public UUID getGeneratedId(Book book) {
        return UUID.randomUUID();
    }

    private static final String SQL_SELECT =
            "SELECT " +
                    "B.id AS book_id, " +
                    "B.name AS book_name, " +
                    "B.published AS book_published, " +
                    "B.author AS book_author, " +
                    "B.number_pages AS book_number_pages, " +
                    "B.version AS book_version ";

    private static final String SQL_SELECT_WITH_MAPPING =
            SQL_SELECT + ", " +
                    "L.id AS library_id, " +
                    "L.name AS library_name, " +
                    "L.address AS library_address , " +
                    "L.version AS library_version, " +
                    "BL.version AS book_library_version ";

    private String sqlGetByIdWithLibraries() {
        return SQL_SELECT_WITH_MAPPING + " " +
                "FROM  books AS B " +
                "LEFT JOIN books_libraries AS BL ON B.id = BL.book_id " +
                "LEFT JOIN libraries AS L ON L.id = BL.library_id " +
                "WHERE B.id = :id";
    }

    @Override
    protected String sqlGetById() {
        return SQL_SELECT + " " +
                "FROM books AS B  WHERE id = :id";
    }

    @Override
    protected String sqlSave() {
        return "INSERT INTO books (id, name, published, author, number_pages, version) VALUES (:id, :name, :published, :author, :number_pages, :version)";
    }

    @Override
    protected String sqlSearch() {
        return SQL_SELECT + " " +
                "FROM books AS B " +
                "WHERE B.name = COALESCE(:name,B.name) and B.author = COALESCE(:author,B.author)";
    }

    @Override
    protected String sqlUpdate() {
        return "UPDATE books SET id = :id, name = :name, published = :published, author = :author, number_pages = :number_pages, version = :version WHERE id = :id";
    }

    @Override
    protected String sqlDelete() {
        return "DELETE FROM books WHERE id = :id";
    }

    @Override
    public Book getByIdWithLibraries(UUID id) {
        BinaryOperator<Book> reducedBooks = (book, book2) -> {
            book.getLibraries().addAll(book2.getLibraries());
            return book;
        };
        return getByIdWithJoins(id, sqlGetByIdWithLibraries(), sqlBookWithLibrariesMapper, reducedBooks);
    }

    @Override
    protected MapSqlParameterSource getCommonParametersSource(Book book) {
        return super.getCommonParametersSource(book)
                .addValue("name", book.getName())
                .addValue("published", book.getPublished())
                .addValue("author", book.getAuthor())
                .addValue("number_pages", book.getNumberPages());
    }

    @Override
    public List<Book> searchBook(String name, String author) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        searchCriterias.put("author", author);
        return search(searchCriterias);
    }

}
