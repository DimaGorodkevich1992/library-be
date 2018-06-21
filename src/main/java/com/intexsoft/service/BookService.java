package com.intexsoft.service;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.model.Library;
import com.intexsoft.repository.BookRepository;
import com.intexsoft.repository.LibraryBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Objects;
import java.util.UUID;

@Service
public class BookService extends CommonService<Book, UUID> {

    @Autowired
    private LibraryBookRepository libraryBookRepository;

    @Autowired
    private BookRepository bookRepository;

    public Observable<Book> searchBook(String name, String author) {
        return Observable.fromCallable(() -> Observable.from(bookRepository.searchBook(name, author)))
                .compose(Observable::merge)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Book> getByIdWithLibrary(UUID id) {
        return Observable.just(id)
                .map(bookRepository::getByIdWithLibraries)
                .filter(Objects::nonNull)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Library> searchLibraries(UUID bookId) {
        return Observable.just(bookId)
                .compose(observable -> Observable.from(libraryBookRepository.searchLibraries(bookId)))
                .subscribeOn(Schedulers.io());

    }

    public Observable<Book> addLibrary(UUID bookId, UUID libraryId) {
        return Observable.fromCallable(() -> libraryBookRepository.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(bookId)
                        .setLibraryId(libraryId))))
                .map(bl -> bookRepository.getByIdWithLibraries(bl.getId().getBookId()))
                .subscribeOn(Schedulers.io());
    }

}
