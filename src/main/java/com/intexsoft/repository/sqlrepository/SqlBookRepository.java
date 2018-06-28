package com.intexsoft.repository.sqlrepository;

import com.intexsoft.model.Book;
import com.intexsoft.repository.BookRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql")
public class SqlBookRepository extends SqlCommonRepository<Book, UUID> implements BookRepository {

    private static final String SQL_SELECT_WITH_MAPPING =
            "SELECT " +
                    "books.id AS book_id, " +
                    "books.name AS book_name, " +
                    "books.published AS book_published " +
                    "books.author AS book_author, " +
                    "books.number_pages AS book_number_pages, " +
                    "books.version AS book_version, " +
                    "libraries.id AS library_id, " +
                    "libraries.name AS library_name, " +
                    "libraries.address AS library_address " +
                    "libraries.version AS library_version";

    @Override
    public UUID getGeneratedId(Book book) {
        return UUID.randomUUID();
    }

    @Override
    public Book getByIdWithLibraries(UUID id) {
        return getById(id,sqlGetByIdWithItems());
    }

    @Override
    protected String sqlGetByIdWithItems() {
        return SQL_SELECT_WITH_MAPPING + " " +
                "FROM books, libraries " +
                "INNER JOIN books_libraries AS B ON B.library_id = libraries.id " +
                "WHERE books.id = :id";
    }


    @Override
    protected String sqlGetById() {
        return "SELECT * FROM books WHERE id = :id";
    }

    @Override
    protected String sqlSave() {
        return "INSERT INTO books (id, name, published, author, number_pages, version) VALUES (:id, :name, :published, :author, :number_pages, :version)";
    }

    @Override
    protected String sqlSearch() {
        return SQL_SELECT_WITH_MAPPING + " " +
                "FROM books INNER JOIN libraries ON (books.libraries_id = libraries.id) WHERE books.name = COALESCE(:name,books.name) and author = COALESCE(:author,author)";
    }

    @Override
    protected String sqlUpdate() {
        return "UPDATE books SET id = :id, name = :name, published = :published, author = :author, number_pages = :number_pages, version = :version, library_id = library_id WHERE id = :id";
    }

    @Override
    protected String sqlDelete() {
        return "DELETE FROM books WHERE id = :id";
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
