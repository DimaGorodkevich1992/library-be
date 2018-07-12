package com.intexsoft.service;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.repository.BookLibraryRepository;
import com.intexsoft.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Objects;
import java.util.UUID;

@Service
public class BookService extends CommonService<Book, UUID> {

    @Autowired
    private BookLibraryRepository bookLibraryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    protected String searchCacheId() {
        return "searchBooks";
    }

    @Override
    protected String commonCacheId() {
        return "commonBooks";
    }

    @Override
    protected String withItemsCacheId() {
        return "bookWithItems";
    }

    @Override
    protected Class<Book> getEntityClass() {
        return Book.class;
    }

    public Observable<Book> searchBook(String name, String author) {
        return  Observable.fromCallable(() -> bookRepository.searchBook(name, author))
                .map(Observable::from)
                .compose(Observable::merge)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Book> getByIdWithLibrary(UUID id) {
        return Observable.just(id)
                .map(bookRepository::getByIdWithLibraries)
                .filter(Objects::nonNull)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Book> addLibrary(UUID bookId, UUID libraryId) {
        return Observable.fromCallable(() -> bookLibraryRepository.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(bookId)
                        .setLibraryId(libraryId))))
                .map(bl -> bookRepository.getByIdWithLibraries(bl.getId().getBookId()))
                .subscribeOn(Schedulers.io());
    }

}
