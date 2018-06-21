package com.intexsoft.repository.jparepository;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.model.Library;
import com.intexsoft.repository.LibraryBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbJpa")
public class JpaLibraryBookRepository extends JpaCommonRepository<BookLibrary, BookLibraryId> implements LibraryBookRepository {
    
    @Autowired
    private EntityManager em;

    @Override
    protected Class<BookLibrary> getModelClass() {
        return BookLibrary.class;
    }

    @Override
    public List<Book> searchBooks(UUID libraryId) {
        return searchAttachment(new Library().setId(libraryId), Book.class, "book", "library");
    }

    @Override
    public List<Library> searchLibraries(UUID bookId) {
        return searchAttachment(new Book().setId(bookId), Library.class, "library", "book");
    }

    private <E, C> List<E> searchAttachment(C criteriaForSearchEntity, Class<E> attachment, String joinToName, String joinFromName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(attachment);
        Root<BookLibrary> relation = query.from(BookLibrary.class);
        Join<BookLibrary, E> books = relation.join(joinToName);
        query.select(books).where(cb.equal(relation.get(joinFromName), criteriaForSearchEntity));
        return em.createQuery(query).getResultList();
    }

}


