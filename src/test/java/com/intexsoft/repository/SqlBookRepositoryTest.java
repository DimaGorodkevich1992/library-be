package com.intexsoft.repository;

import com.intexsoft.ClientServiceRunner;
import com.intexsoft.model.Book;
import com.intexsoft.repository.sqlrepository.SqlBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@Slf4j
@ContextConfiguration(classes = {ClientServiceRunner.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource("classpath:sql-repository.properties")
public class SqlBookRepositoryTest implements InitializingBean {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private SqlBookRepository sqlBookRepositoryTest;

    private UUID id = UUID.fromString("d3e32870-f061-4a86-8e65-a97a8bd42d4e");
    private UUID wrongId = UUID.fromString("2faa9b95-391e-491b-bb69-4b25ba07a352");
    private UUID id2 = UUID.fromString("e5aaccf8-0cca-4f38-a30d-747eac1d2adc");
    private UUID id3 = UUID.fromString("d6d1d9ec-ab8f-4afa-935e-f636f10b45b2");
    private UUID library = UUID.fromString("31d9ccae-6fed-4a84-b979-43b757e6c146");

    public Book getBook() {
        String dateString = "2018-04-24";
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            log.warn("Cannot parse date", e);
        }
        return new Book()
                .setId(id)
                .setName("name")
                .setAuthor("author")
                .setPublished(date)
                .setNumberPages(300)
                .setVersion(1);
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
    public void correctSave() {
    }

    @Test
    public void getByIdCorrectId() {
    }

    @Test
    public void getByIdIncorrectId() {
    }

    @Test
    public void getByIdWithItemsIncorrectId() {
    }

    @Test
    public void getByIdWithEmptyItems() {
    }

    @Test
    public void getByidWithFullItems() {
    }

    @Test
    public void updateCorrect() {
    }

    @Test
    public void updateIncorrectVersion() {
    }

    @Test
    public void updateDuplicateName() {
    }

    @Test
    public void searchByNull() {
    }

    @Test
    public void searchByName() {
    }

    @Test
    public void searchByAuthor() {
    }

    @Test
    public void searchByNameAndAuthors() {
    }

    @Test
    public void addLibrary() {
    }

    @Test
    public void addDuplicateLibrary() {
    }

    @Test
    public void addNotExistingLibrary() {
    }

    @Test(expected = DuplicateKeyException.class)
    public void saveBookDuplicateName() {
        sqlBookRepositoryTest.save(getBook());
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
        List<Book> wantedList = sqlBookRepositoryTest.searchBook(null, null);
        assertEquals(2, wantedList.size());
    }

    @Test
    public void searchBookByName() {
        List<Book> wantedList = sqlBookRepositoryTest.searchBook("name1", null);
        assertEquals("name1", wantedList.get(0).getName());
    }

    @Test
    public void searchBookByAuthor() {
        List<Book> wantedList = sqlBookRepositoryTest.searchBook(null, "author");
        wantedList.forEach(s -> assertEquals("author", s.getAuthor()));
    }

    @Test
    public void searchBookByNameAndAuthor() {
        List<Book> wantedList = sqlBookRepositoryTest.searchBook("name", "author");
        assertEquals("name", wantedList.get(0).getName());
        assertEquals("author", wantedList.get(0).getAuthor());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("");
    }
}
