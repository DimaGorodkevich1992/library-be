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
public class BookService extends CommonService<Book, UUID, Book> {
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

    public Observable<Book> addLibrary(BookLibraryId id) {
        return Observable.just(id)
                .map(s -> libraryBookRepository.save(new BookLibrary()
                        .setId(s)
                        .setVersion(1)
                        .setBook(new Book().setId(s.getBookId()))
                        .setLibrary(new Library().setId(s.getLibraryId()))))
                .map(BookLibrary::getBook)
                .subscribeOn(Schedulers.io());
    }

    @Override
    protected UUID getGeneratedId() {
        return UUID.randomUUID();
    }
}
