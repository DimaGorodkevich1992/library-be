/*
package com.intexsoft.repository;

import com.intexsoft.config.AppConfigTest;
import com.intexsoft.model.Book;
import com.intexsoft.repository.jparepository.JpaBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppConfigTest.class)
@Transactional
public class JpaBookRepositoryTest  {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private JpaBookRepository jpaBookRepositoryTest;

    private UUID id = UUID.fromString("d3e32870-f061-4a86-8e65-a97a8bd42d4e");
    private UUID wrongId = UUID.fromString("2faa9b95-391e-491b-bb69-4b25ba07a352");
    private UUID id2 = UUID.fromString("e5aaccf8-0cca-4f38-a30d-747eac1d2adc");
    private UUID id3 = UUID.fromString("d6d1d9ec-ab8f-4afa-935e-f636f10b45b2");

    public Book getBook() {
        Book book = new Book();
        String dateString = "2018-04-24";
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            log.warn("Cannot parse date", e);
        }
        book.setId(id);
        book.setName("name");
        book.setAuthor("author");
        book.setPublished(date);
        book.setNumberPages(300);
        book.setVersion(1);
        return book;
    }

    @Before
    public void initializedBook() {
        jpaBookRepositoryTest.save(getBook());
        Book book = new Book();
        BeanUtils.copyProperties(getBook(), book);
        book.setName("name1");
        book.setId(id2);
        jpaBookRepositoryTest.save(book);
    }

    @After
    public void deleteBook() {
        jpaBookRepositoryTest.deleteById(id);
        jpaBookRepositoryTest.deleteById(id2);
    }


    @Test
    public void saveBookCorrect() {
        Book book = new Book();
        BeanUtils.copyProperties(getBook(), book);
        book.setName("name2");
        book.setId(id3);
        jpaBookRepositoryTest.save(book);
        Book wantedBook = jpaBookRepositoryTest.getById(id3);
        assertEquals(book, jpaBookRepositoryTest.getById(id3));
        jpaBookRepositoryTest.deleteById(id3);
    }
   

    @Test(expected = EntityExistsException.class)
    public void saveBookDuplicateName() {
        jpaBookRepositoryTest.save(getBook());
    }

    @Test
    public void getByIdCaseCorrectId() {
        Book wantedBook = jpaBookRepositoryTest.getById(id);
        assertEquals(getBook(), wantedBook);
    }

    @Test
    public void getByIdCaseIncorrectId() {
       assertNull(jpaBookRepositoryTest.getById(wrongId));

    }

    @Test
    public void updateBookCorrect() {
        Book book = new Book();
        BeanUtils.copyProperties(getBook(), book);
        book.setAuthor("tolkien");
        jpaBookRepositoryTest.update(book);
        assertEquals(2, jpaBookRepositoryTest.getById(id).getVersion());
    }

    @Test(expected = StaleObjectStateException.class)
    public void updateBookIncorrectVersion() {
        Book book = new Book();
        BeanUtils.copyProperties(getBook(), book);
        book.setVersion(33);
        jpaBookRepositoryTest.update(book);
    }

    @Test
    public void searchBookByNull() {
        List<Book> wantedList = jpaBookRepositoryTest.search(null, null);
        assertEquals(2, wantedList.size());
    }

    @Test
    public void searchBookByName() {
        List<Book> wantedList = jpaBookRepositoryTest.search("name1", null);
        assertEquals("name1", wantedList.get(0).getName());
    }

    @Test
    public void searchBookByAuthor() {
        List<Book> wantedList = jpaBookRepositoryTest.search(null, "author");
        wantedList.forEach(s -> assertEquals("author", s.getAuthor()));
    }

    @Test
    public void searchBookByNameAndAuthor() {
        List<Book> wantedList = jpaBookRepositoryTest.search("name", "author");
        assertEquals("name", wantedList.get(0).getName());
        assertEquals("author", wantedList.get(0).getAuthor());
    }

    @Test
    public void deleteByIdCorrectId() {
        Book book = new Book();
        BeanUtils.copyProperties(getBook(), book);
        book.setName("name2");
        book.setId(id3);
        jpaBookRepositoryTest.save(book);
        jpaBookRepositoryTest.deleteById(id3);
        jpaBookRepositoryTest.getById(id3);
    }


}
*/
