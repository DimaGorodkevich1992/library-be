package com.intexsoft.repository.jparepository;


import com.intexsoft.model.Book;
import com.intexsoft.model.Library;
import com.intexsoft.model.LibraryBook;
import com.intexsoft.repository.BookRepository;
import com.intexsoft.repository.LibraryBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Root;
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
    public List<Book> searchBook(String name, String author) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        searchCriterias.put("author", author);
        return search(searchCriterias);

    }


    @Override
    public Book getById(UUID id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> from = query.from(Book.class);
        Fetch<LibraryBook,Library> fetch = from.fetch("libraries",);
        query.select(from).where(cb.equal(from.get("id"),id));
        Book book2 = em.createQuery(query).getSingleResult();
        return em.createQuery(query).getSingleResult();
    }

}
