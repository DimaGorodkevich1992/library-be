
package com.intexsoft.repository;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.model.Library;
import com.intexsoft.repository.jparepository.JpaBookLibraryRepository;
import com.intexsoft.repository.jparepository.JpaBookRepository;
import com.intexsoft.repository.jparepository.JpaLibraryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource("classpath:jpa-repository.properties")
public class JpaBookRepositoryTest {

    @Autowired
    private JpaBookRepository bookRepositoryTest;
    @Autowired
    private JpaLibraryRepository libraryRepositoryTest;
    @Autowired
    private JpaBookLibraryRepository bookLibraryRepositoryTest;

    private UUID correctId = UUID.fromString("d3e32870-f061-4a86-8e65-a97a8bd42d4e");
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
                .setId(correctId)
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
        bookRepositoryTest.deleteById(correctId);
        libraryRepositoryTest.deleteById(libraryId);
    }

    @Test
    public void generatedIdWhereIdNull() {
        assertNotNull(bookRepositoryTest.getGeneratedId(new Book().setId(null)));
    }

    @Test
    public void generatedIdWhereIdNotNull() {
        assertEquals(correctId, bookRepositoryTest.getGeneratedId(new Book().setId(correctId)));
    }

    @Test
    public void correctSave() {
        Book book = new Book()
                .setId(saveId)
                .setName("correctSave")
                .setAuthor("correctSave")
                .setPublished(getDate())
                .setNumberPages(500);
        assertEquals(book, bookRepositoryTest.save(book));
        bookRepositoryTest.deleteById(saveId);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void incorrectSaveDuplicateName() {
        bookRepositoryTest.save(getBook());
    }

    @Test
    @Transactional
    public void getByIdCorrectId() {
        assertEquals(getBook().setVersion(1), bookRepositoryTest.getById(correctId));
    }

    @Test
    @Transactional
    public void getByIdIncorrectId() {
        assertNull(bookRepositoryTest.getById(wrongId));
    }

    @Test
    public void getByIdWithItemsIncorrectId() {
        assertNull(bookRepositoryTest.getByIdWithLibraries(wrongId));
    }

    @Test
    public void getByIdWithEmptyItems() {
        assertEquals(Collections.emptySet(), bookRepositoryTest.getByIdWithLibraries(correctId).getLibraries());
    }

    @Test
    public void addLibraryAndGetByIdWithFullItems() {
        BookLibrary saveBookLibrary = new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(correctId)
                        .setLibraryId(libraryId));
        bookLibraryRepositoryTest.save(saveBookLibrary);
        BookLibrary bookLibrary = new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(correctId)
                        .setLibraryId(libraryId))
                .setVersion(1);
        assertEquals(bookLibrary, bookRepositoryTest.getByIdWithLibraries(correctId).getLibraries().iterator().next());
    }

    @Test
    public void updateCorrect() {
        Book bookToUpdate = new Book()
                .setId(correctId)
                .setPublished(getDate())
                .setNumberPages(300)
                .setName("updateCorrect")
                .setAuthor("updateCorrect")
                .setVersion(1);
        bookRepositoryTest.update(bookToUpdate);
        assertEquals("updateCorrect", bookRepositoryTest.getById(correctId).getName());
        assertEquals("updateCorrect", bookRepositoryTest.getById(correctId).getAuthor());
        assertEquals(2, bookRepositoryTest.getById(correctId).getVersion());
        assertEquals(new Integer(300), bookRepositoryTest.getById(correctId).getNumberPages());
    }

    @Test(expected = OptimisticLockException.class)
    public void updateIncorrectVersion() {
        Book bookToUpdate = new Book();
        BeanUtils.copyProperties(getBook(), bookToUpdate);
        bookToUpdate
                .setName("updateCorrect")
                .setAuthor("updateCorrect")
                .setVersion(33);
        bookRepositoryTest.update(bookToUpdate);
    }

    @Test
    public void searchByNull() {
        UUID id = UUID.fromString("f0fab8ec-be93-4446-ad41-aa8e756bacb5");
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

    @Test(expected = DataIntegrityViolationException.class)
    public void addDuplicateLibrary() {
        bookLibraryRepositoryTest.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(correctId)
                        .setLibraryId(libraryId)));
        bookLibraryRepositoryTest.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(correctId)
                        .setLibraryId(libraryId)));

    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addNotExistingLibrary() {
        bookLibraryRepositoryTest.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(correctId)
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

