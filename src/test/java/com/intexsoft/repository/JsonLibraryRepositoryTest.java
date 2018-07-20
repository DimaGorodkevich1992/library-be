package com.intexsoft.repository;

import com.intexsoft.exception.DuplicateNameException;
import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.model.Library;
import com.intexsoft.repository.jsonrepository.JsonBookLibraryRepository;
import com.intexsoft.repository.jsonrepository.JsonBookRepository;
import com.intexsoft.repository.jsonrepository.JsonLibraryRepository;
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
@TestPropertySource("classpath:json-repository.properties")
public class JsonLibraryRepositoryTest {
    @Autowired
    private JsonLibraryRepository libraryRepositoryTest;
    @Autowired
    private JsonBookRepository bookRepositoryTest;
    @Autowired
    private JsonBookLibraryRepository bookLibraryRepositoryTest;

    private UUID libraryId = UUID.fromString("d3e32870-f061-4a86-8e65-a97a8bd42d4e");
    private UUID wrongId = UUID.fromString("2faa9b95-391e-491b-bb69-4b25ba07a352");
    private UUID saveId = UUID.fromString("d6d1d9ec-ab8f-4afa-935e-f636f10b45b2");
    private UUID bookId = UUID.fromString("31d9ccae-6fed-4a84-b979-43b757e6c146");
    private String name = "name";
    private String address = "author";

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

    private Library getLibrary() {
        return new Library()
                .setId(libraryId)
                .setName(name)
                .setAddress(address);
    }

    @Before
    public void initializedBookAndLibrary() {
        bookRepositoryTest.save(new Book()
                .setId(bookId)
                .setName("initializeBook")
                .setAuthor("initializeBook")
                .setPublished(getDate())
                .setNumberPages(300) );
        libraryRepositoryTest.save(getLibrary());
    }

    @After
    public void deleteLibrary() {
        libraryRepositoryTest.deleteById(libraryId);
        bookRepositoryTest.deleteById(bookId);
    }

    @Test
    public void generatedIdWhereIdNull() {
        assertNotNull(libraryRepositoryTest.getGeneratedId(new Library().setId(null)));
    }

    @Test
    public void generatedIdWhereIdNotNull() {
        assertEquals(libraryId, libraryRepositoryTest.getGeneratedId(new Library().setId(libraryId)));
    }

    @Test
    public void correctSave() {
        Library library = new Library()
                .setId(saveId)
                .setName("correctSave")
                .setAddress("correctSave");
        assertEquals(library, libraryRepositoryTest.save(library));
        libraryRepositoryTest.deleteById(saveId);
    }

    @Test(expected = DuplicateNameException.class)
    public void incorrectSaveDuplicateName() {
        libraryRepositoryTest.save(getLibrary());
    }

    @Test
    @Transactional
    public void getByIdCorrectId() {
        assertEquals(getLibrary().setVersion(1), libraryRepositoryTest.getById(libraryId));
    }

    @Test
    @Transactional
    public void getByIdIncorrectId() {
        assertNull(libraryRepositoryTest.getById(wrongId));
    }

    @Test
    public void getByIdWithItemsIncorrectId() {
        assertNull(libraryRepositoryTest.getById(wrongId));
    }

    @Test
    public void getByIdWithEmptyItems() {
        assertEquals(Collections.emptySet(), libraryRepositoryTest.getByIdWithBooks(libraryId).getBooks());
    }

    @Test
    public void addLibraryAndGetByIdWithFullItems() {
        BookLibrary saveBookLibrary = new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(bookId)
                        .setLibraryId(libraryId));
        bookLibraryRepositoryTest.save(saveBookLibrary);

        assertEquals(saveBookLibrary, libraryRepositoryTest.getByIdWithBooks(libraryId).getBooks().iterator().next());
    }

    @Test
    public void updateCorrect() {
        Library libraryToUpdate = new Library()
                .setId(libraryId)
                .setName("updateCorrect")
                .setAddress("updateCorrect")
                .setVersion(1);

        libraryRepositoryTest.update(libraryToUpdate);
        assertEquals("updateCorrect", libraryRepositoryTest.getById(libraryId).getName());
        assertEquals("updateCorrect", libraryRepositoryTest.getById(libraryId).getAddress());
        assertEquals(2, libraryRepositoryTest.getById(libraryId).getVersion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateIncorrectVersion() {
        Library libraryToUpdate = new Library();
        BeanUtils.copyProperties(getLibrary(), libraryToUpdate);
        libraryToUpdate
                .setName("updateCorrect")
                .setAddress("updateCorrect")
                .setVersion(33);
        libraryRepositoryTest.update(libraryToUpdate);
    }

    @Test
    public void searchByNull() {
        UUID id = UUID.fromString("f0fab8ec-be93-4446-ad41-aa8e756bacb5");
        libraryRepositoryTest.save(new Library()
                .setId(id)
                .setName("searchByNull")
                .setAddress("searchByNull"));
        List<Library> wantedList = libraryRepositoryTest.searchLibrary(null, null);
        assertEquals(2, wantedList.size());
        libraryRepositoryTest.deleteById(id);
    }

    @Test
    public void searchByName() {
        List<Library> wantedList = libraryRepositoryTest.searchLibrary(name, null);
        assertEquals(name, wantedList.get(0).getName());
    }

    @Test
    public void searchByAuthor() {
        List<Library> wantedList = libraryRepositoryTest.searchLibrary(null, address);
        wantedList.forEach(s -> assertEquals(address, s.getAddress()));
    }

    @Test
    public void searchByNameAndAuthors() {
        List<Library> wantedList = libraryRepositoryTest.searchLibrary(name, address);
        assertEquals(name, wantedList.get(0).getName());
        assertEquals(address, wantedList.get(0).getAddress());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addDuplicateBook() {
        bookLibraryRepositoryTest.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(libraryId)
                        .setLibraryId(bookId)));
        bookLibraryRepositoryTest.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(libraryId)
                        .setLibraryId(bookId)));

    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addNotExistingBook() {
        bookLibraryRepositoryTest.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(libraryId)
                        .setLibraryId(wrongId)));
    }

    @Test
    public void correctDelete() {
        UUID id2 = UUID.fromString("e5aaccf8-0cca-4f38-a30d-747eac1d2adc");
        libraryRepositoryTest.save(new Library()
                .setId(id2)
                .setName("correctDelete")
                .setAddress("correctDelete"));
        libraryRepositoryTest.deleteById(id2);
        assertNull(libraryRepositoryTest.getById(id2));

    }

}
