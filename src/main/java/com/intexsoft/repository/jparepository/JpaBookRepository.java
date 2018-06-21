package com.intexsoft.repository.jparepository;

import com.intexsoft.model.Book;
import com.intexsoft.repository.BookRepository;
import com.intexsoft.repository.LibraryBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbJpa")
public class JpaBookRepository extends JpaCommonRepository<Book, UUID> implements BookRepository {

    @Autowired
    private LibraryBookRepository libraryBookRepository;

    @Override
    public UUID getGeneratedId(Book book) {
        return UUID.randomUUID();
    }

    @Override
    protected Class<Book> getModelClass() {
        return Book.class;
    }

    @Override
    public Book getByIdWithLibraries(UUID id) {
        List<String> fetchCriterias = new ArrayList<>();
        fetchCriterias.add(0, "libraries");
        fetchCriterias.add(1, "library");
        return getById(id, fetchCriterias);
    }

    @Override
    public List<Book> searchBook(String name, String author) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        searchCriterias.put("author", author);
        return search(searchCriterias);

    }

}
