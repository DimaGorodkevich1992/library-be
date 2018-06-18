package com.intexsoft.service;

import com.intexsoft.model.Book;
import com.intexsoft.model.Library;
import com.intexsoft.model.LibraryBook;
import com.intexsoft.model.LibraryBookId;
import com.intexsoft.repository.BookRepository;
import com.intexsoft.repository.CommonRepository;
import com.intexsoft.repository.LibraryBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.UUID;


@Service
public class BookService extends CommonService<Book, UUID> {
    @Autowired
    private LibraryBookRepository libraryBookRepository;

    @Autowired
    private BookRepository bookRepository;

    public Observable<Book> searchBook(String name, String author) {  //todo
        return Observable.from(bookRepository.searchBook(name, author))
                .subscribeOn(Schedulers.io());
    }

    public Observable<Library> searchLibraries(UUID bookId){
        return Observable.from(libraryBookRepository.searchLibraries(bookId))
                .subscribeOn(Schedulers.io());
    }


    public Observable<Book> addLibrary(LibraryBookId id) {
        LibraryBook libraryBook = new LibraryBook();
        libraryBook.setId(id);
        Book book = new Book();
        book.setId(id.getBookId());
        Library library = new Library();
        library.setId(id.getLibraryId());
        libraryBook.setBook(book);
        libraryBook.setLibrary(library);
        libraryBook.setVersion(1);
        return Observable.just(libraryBookRepository.save(libraryBook))
                .map(LibraryBook::getBook)
                .subscribeOn(Schedulers.io());
    }

    @Override
    protected UUID getGeneratedId() {
        return UUID.randomUUID();
    }
}
