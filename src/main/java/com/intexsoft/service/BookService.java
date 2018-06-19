package com.intexsoft.service;

import com.intexsoft.model.Book;
import com.intexsoft.model.Library;
import com.intexsoft.model.LibraryBook;
import com.intexsoft.model.LibraryBookId;
import com.intexsoft.repository.BookRepository;
import com.intexsoft.repository.LibraryBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.UUID;


@Service
public class BookService extends CommonService<Book, UUID, Book> {
    @Autowired
    private LibraryBookRepository libraryBookRepository;

    @Autowired
    private BookRepository bookRepository;

    public Observable<Book> searchBook1(String name, String author) {
        return Observable.just(name, author)
                .compose(observable ->  Observable.from(bookRepository.searchBook(name,author)))
                .subscribeOn(Schedulers.io());
    }

    public Observable<Book> searchBook(String name, String author) {
        return Observable.fromCallable (() -> Observable.from(bookRepository.searchBook(name,author)))
                .compose(Observable::merge)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Library> searchLibraries(UUID bookId) {
        return Observable.just(bookId)
                .compose(observable -> Observable.from(libraryBookRepository.searchLibraries(bookId)))
                .subscribeOn(Schedulers.io());

    }


    public Observable<Book> addLibrary(LibraryBookId id) {


        LibraryBook libraryBook = new LibraryBook();
        libraryBook.setId(id);
        Book book = new Book();
        book.setId(id.getBookId());
        Library library = new Library();
        library.setId(id.getLibraryId());

        return Observable.just(libraryBookRepository.save(libraryBook))
                .map(LibraryBook::getBook)
                .subscribeOn(Schedulers.io());
    }

    @Override
    protected UUID getGeneratedId() {
        return UUID.randomUUID();
    }
}
