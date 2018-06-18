package com.intexsoft.repository.jparepository;

import com.intexsoft.model.Book;
import com.intexsoft.model.Library;
import com.intexsoft.model.LibraryBook;
import com.intexsoft.model.LibraryBookId;
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
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbJpa", matchIfMissing = false)
public class JpaLibraryBookRepository extends JpaCommonRepository<LibraryBook, LibraryBookId> implements LibraryBookRepository {
    @Autowired
    private EntityManager em;

    @Override
    protected Class<LibraryBook> getModelClass() {
        return LibraryBook.class;
    }

    @Override
    public List<Book> searchBooks(UUID libraryId) {
        Library tmpLibrary = new Library();
        tmpLibrary.setId(libraryId);
        return searchAttachment(tmpLibrary, Book.class, "book", "library");
    }

    @Override
    public List<Library> searchLibraries(UUID bookId) {
        Book tmpBook = new Book();
        tmpBook.setId(bookId);
        return searchAttachment(tmpBook, Library.class, "library", "book");
    }


    private <E, C> List<E> searchAttachment(C criteriaForSearchEntity, Class<E> attachment, String joinToName, String joinFromName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(attachment);
        Root<LibraryBook> relation = query.from(LibraryBook.class);
        Join<LibraryBook, E> books = relation.join(joinToName);
        query.select(books).where(cb.equal(relation.get(joinFromName), criteriaForSearchEntity));
        return em.createQuery(query).getResultList();
    }


}


