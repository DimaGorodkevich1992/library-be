package com.intexsoft.repository.jparepository;

import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.repository.BookLibraryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbJpa")
public class JpaBookLibraryRepository extends JpaCommonRepository<BookLibrary, BookLibraryId> implements BookLibraryRepository {

    @Override
    protected Class<BookLibrary> getModelClass() {
        return BookLibrary.class;
    }

    /*private <E, C> List<E> searchAttachment(C criteriaForSearchEntity, Class<E> attachment, String joinToName, String joinFromName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(attachment);
        Root<BookLibrary> relation = query.from(BookLibrary.class);
        Join<BookLibrary, E> books = relation.join(joinToName);
        query.select(books).where(cb.equal(relation.get(joinFromName), criteriaForSearchEntity));
        return em.createQuery(query).getResultList();
    }
*/
}


