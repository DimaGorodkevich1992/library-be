/*
package com.intexsoft.repository;


import com.intexsoft.ClientServiceRunner;
import com.intexsoft.repository.sqlrepository.SqlBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

@Slf4j
@ContextConfiguration(classes = {ClientServiceRunner.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest("classpath:application.properties")
public class SqlBookRepositoryTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private SqlBookRepository sqlBookRepositoryTest;

    private UUID id = UUID.fromString("d3e32870-f061-4a86-8e65-a97a8bd42d4e");
    private UUID wrongId = UUID.fromString("2faa9b95-391e-491b-bb69-4b25ba07a352");
    private UUID id2 = UUID.fromString("e5aaccf8-0cca-4f38-a30d-747eac1d2adc");
    private UUID id3 = UUID.fromString("d6d1d9ec-ab8f-4afa-935e-f636f10b45b2");
    private UUID library = UUID.fromString("31d9ccae-6fed-4a84-b979-43b757e6c146");

    @Test
    public void testConfig() {

    }

    public Book getBook() {
        Library library = new Library();
        library.setId(library);

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
        sqlBookRepositoryTest.save(getBook());
        Book book = new Book();
        BeanUtils.copyProperties(getBook(), book);
        book.setName("name1");
        book.setId(id2);
        sqlBookRepositoryTest.save(book);

    }

    @After
    public void deleteBook() {
        sqlBookRepositoryTest.deleteById(id);
        sqlBookRepositoryTest.deleteById(id2);
    }


    @Test
    public void saveBookCorrect() {
        Book book = new Book();
        BeanUtils.copyProperties(getBook(), book);
        book.setName("name2");
        book.setId(id3);
        sqlBookRepositoryTest.save(book);
        assertEquals(book, sqlBookRepositoryTest.getById(id3));
        sqlBookRepositoryTest.deleteById(id3);
    }

    @Test(expected = DuplicateKeyException.class)
    public void saveBookDuplicateName() {
        sqlBookRepositoryTest.save(getBook());
    }

    @Test
    public void getByIdCaseCorrectId() {
        Book wantedBook = sqlBookRepositoryTest.getById(id);
        assertEquals(getBook(), wantedBook);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getByIdCaseIncorrectId() {
        sqlBookRepositoryTest.getById(wrongId);

    }

    @Test
    public void updateBookCorrect() {
        Book book = new Book();
        BeanUtils.copyProperties(getBook(), book);
        book.setAuthor("tolkien");
        sqlBookRepositoryTest.update(book);
        assertEquals(2, sqlBookRepositoryTest.getById(id).getVersion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateBookIncorrectVersion() {
        Book book = new Book();
        BeanUtils.copyProperties(getBook(), book);
        book.setVersion(33);
        sqlBookRepositoryTest.update(book);
    }

    @Test
    public void searchBookByNull() {
        List<Book> wantedList = sqlBookRepositoryTest.searchLibrary(null, null);
        assertEquals(2, wantedList.size());
    }

    @Test
    public void searchBookByName() {
        List<Book> wantedList = sqlBookRepositoryTest.searchLibrary("name1", null);
        assertEquals("name1", wantedList.get(0).getName());
    }

    @Test
    public void searchBookByAuthor() {
        List<Book> wantedList = sqlBookRepositoryTest.searchLibrary(null, "author");
        wantedList.forEach(s -> assertEquals("author", s.getAuthor()));
    }

    @Test
    public void searchBookByNameAndAuthor() {
        List<Book> wantedList = sqlBookRepositoryTest.searchLibrary("name", "author");
        assertEquals("name", wantedList.get(0).getName());
        assertEquals("author", wantedList.get(0).getAuthor());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteByIdCorrectId() {
        Book book = new Book();
        BeanUtils.copyProperties(getBook(), book);
        book.setName("name2");
        book.setId(id3);
        sqlBookRepositoryTest.save(book);
        sqlBookRepositoryTest.deleteById(id3);
        sqlBookRepositoryTest.getById(id3);
    }
}*/
