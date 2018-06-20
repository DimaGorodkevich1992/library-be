package com.intexsoft.repository.jparepository;


import com.intexsoft.model.Book;
import com.intexsoft.repository.BookRepository;
import com.intexsoft.repository.LibraryBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbJpa", matchIfMissing = false)
public class JpaBookRepository extends JpaCommonRepository<Book, UUID, Book> implements BookRepository {
    @Autowired
    private EntityManager em;

    @Autowired
    private LibraryBookRepository libraryBookRepository;

    @Override
    protected Class<Book> getModelClass() {
        return Book.class;
    }

    @Override
    public Book getByIdWithInformation(UUID id) {
        return getById(id,"libraries","library");
    }

    @Override
    public List<Book> searchBook(String name, String author) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        searchCriterias.put("author", author);
        return search(searchCriterias);

    }

}
