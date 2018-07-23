package com.intexsoft.repository;

import com.intexsoft.ClientServiceRunner;
import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.model.Library;
import com.intexsoft.repository.sqlrepository.SqlBookLibraryRepository;
import com.intexsoft.repository.sqlrepository.SqlBookRepository;
import com.intexsoft.repository.sqlrepository.SqlLibraryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

@Slf4j
@ContextConfiguration(classes = {ClientServiceRunner.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource("classpath:sql-repository.properties")
public class SqlBookRepositoryTest {

    @Autowired
    private SqlBookRepository bookRepositoryTest;
    @Autowired
    private SqlLibraryRepository libraryRepositoryTest;
    @Autowired
    private SqlBookLibraryRepository bookLibraryRepositoryTest;

    private UUID bookId = UUID.fromString("d3e32870-f061-4a86-8e65-a97a8bd42d4e");
    private UUID wrongId = UUID.fromString("2faa9b95-391e-491b-bb69-4b25ba07a352");
    private UUID saveId = UUID.fromString("d6d1d9ec-ab8f-4afa-935e-f636f10b45b2");
    private UUID libraryId = UUID.fromString("31d9ccae-6fed-4a84-b979-43b757e6c146");
    private String name = "name";
    private String author = "author";

    private Date getDate() {
        String dateString = "2018-04-24";
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            log.warn("Cannot parse date", e);
        }
        return date;
    }

    private Book getBook() {
        return new Book()
                .setId(bookId)
                .setName(name)
                .setAuthor(author)
                .setPublished(getDate())
                .setNumberPages(300);
    }

    @Before
    public void initializedBookAndLibrary() {
        libraryRepositoryTest.save(new Library()
                .setId(libraryId)
                .setName("initializeLibrary")
                .setAddress("initializeLibrary"));
        bookRepositoryTest.save(getBook());
    }

    @After
    public void deleteBook() {
        bookRepositoryTest.deleteById(bookId);
        libraryRepositoryTest.deleteById(libraryId);
    }

    @Test
    public void generatedIdWhereIdNull() {
        assertNotNull(bookRepositoryTest.getGeneratedId(new Book().setId(null)));
    }

    @Test
    public void generatedIdWhereIdNotNull() {
        assertEquals(bookId, bookRepositoryTest.getGeneratedId(new Book().setId(bookId)));
    }

    @Test
    public void correctSave() {
        Book book = new Book().setId(saveId)
                .setName("correctSave")
                .setAuthor("correctSave")
                .setPublished(getDate())
                .setNumberPages(500);
        bookRepositoryTest.save(book);
        assertEquals(book, bookRepositoryTest.getById(saveId));
        bookRepositoryTest.deleteById(saveId);
    }

    @Test(expected = DuplicateKeyException.class)
    public void incorrectSaveDuplicateName() {
        bookRepositoryTest.save(getBook());
    }

    @Test
    public void getByIdCorrectId() {
        assertEquals(getBook().setVersion(1), bookRepositoryTest.getById(bookId));
    }

    @Test
    public void getByIdIncorrectId() {
        assertNull(bookRepositoryTest.getById(wrongId));
    }

    @Test
    public void getByIdWithItemsIncorrectId() {
        assertNull(bookRepositoryTest.getByIdWithLibraries(wrongId));
    }

    @Test
    public void getByIdWithEmptyItems() {
        assertEquals(Collections.emptySet(), bookRepositoryTest.getByIdWithLibraries(bookId).getLibraries());
    }

    @Test
    public void addLibraryAndGetByIdWithFullItems() {
        UUID libraryId2 = UUID.fromString("877555ad-7167-40d5-a40f-1ccf45e6b071");
        libraryRepositoryTest.save(new Library()
                .setId(libraryId2)
                .setName("addLibraryAndGetByIdWithFullItems")
                .setAddress("addLibraryAndGetByIdWithFullItems"));
        bookLibraryRepositoryTest.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(bookId)
                        .setLibraryId(libraryId)));
        bookLibraryRepositoryTest.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(bookId)
                        .setLibraryId(libraryId2)));
        BookLibrary bookLibrary = new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(bookId)
                        .setLibraryId(libraryId))
                .setVersion(1);
        BookLibrary bookLibrary2 = new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(bookId)
                        .setLibraryId(libraryId2))
                .setVersion(1);

        long count = bookRepositoryTest.getByIdWithLibraries(bookId).getLibraries().stream()
                .filter(s-> Objects.equals(s, bookLibrary)||Objects.equals(s, bookLibrary2))
                .count();
        assertEquals(2 , count);
        libraryRepositoryTest.deleteById(libraryId2);
    }

    @Test
    public void updateCorrect() {
        Book bookToUpdate = new Book();
        BeanUtils.copyProperties(getBook(), bookToUpdate);
        bookToUpdate
                .setName("updateCorrect")
                .setAuthor("updateCorrect")
                .setVersion(1);
        assertEquals(getBook()
                        .setAuthor("updateCorrect")
                        .setVersion(2)
                        .setName("updateCorrect"),
                bookRepositoryTest.update(bookToUpdate));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateIncorrectVersion() {
        Book bookToUpdate = new Book();
        BeanUtils.copyProperties(getBook(), bookToUpdate);
        bookToUpdate
                .setName("updateCorrect")
                .setAuthor("updateCorrect")
                .setVersion(33);
        assertEquals(getBook()
                        .setAuthor("updateCorrect")
                        .setVersion(33)
                        .setName("updateCorrect"),
                bookRepositoryTest.update(bookToUpdate));
    }

    @Test
    public void searchByNull() {
        UUID id = UUID.fromString("e5aaccf8-0cca-4f38-a30d-747eac1d2adc");
        bookRepositoryTest.save(new Book()
                .setId(id)
                .setName("searchByNull")
                .setAuthor("searchByNull")
                .setPublished(getDate())
                .setNumberPages(500));
        List<Book> wantedList = bookRepositoryTest.searchBook(null, null);
        assertEquals(2, wantedList.size());
        bookRepositoryTest.deleteById(id);
    }

    @Test
    public void searchByName() {
        List<Book> wantedList = bookRepositoryTest.searchBook(name, null);
        assertEquals(name, wantedList.get(0).getName());
    }

    @Test
    public void searchByAuthor() {
        List<Book> wantedList = bookRepositoryTest.searchBook(null, author);
        wantedList.forEach(s -> assertEquals(author, s.getAuthor()));
    }

    @Test
    public void searchByNameAndAuthors() {
        List<Book> wantedList = bookRepositoryTest.searchBook(name, author);
        assertEquals(name, wantedList.get(0).getName());
        assertEquals(author, wantedList.get(0).getAuthor());
    }

    @Test(expected = DuplicateKeyException.class)
    public void addDuplicateLibrary() {
        bookLibraryRepositoryTest.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(bookId)
                        .setLibraryId(libraryId)));
        bookLibraryRepositoryTest.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(bookId)
                        .setLibraryId(libraryId)));

    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addNotExistingLibrary() {
        bookLibraryRepositoryTest.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(bookId)
                        .setLibraryId(wrongId)));
    }

    @Test
    public void correctDelete() {
        UUID id2 = UUID.fromString("e5aaccf8-0cca-4f38-a30d-747eac1d2adc");
        bookRepositoryTest.save(new Book()
                .setId(id2)
                .setName("correctDelete")
                .setAuthor("correctDelete")
                .setPublished(getDate())
                .setNumberPages(500));
        bookRepositoryTest.deleteById(id2);
        assertNull(bookRepositoryTest.getById(id2));

    }

}
