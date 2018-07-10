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

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class BookService extends CommonService<Book, UUID> {

    private static final String CACHE_ID_WITH_ITEMS = "books";

    @Autowired
    private BookLibraryRepository bookLibraryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    protected List<String> getListKeyToDeleteItems(List<String> cacheIdForItems) {
        cacheIdForItems.add(CACHE_ID_WITH_ITEMS);
        return super.getListKeyToDeleteItems(cacheIdForItems);
    }

    @Override
    protected String commonCacheId() {
        return "booksCommon";
    }

    public Observable<Book> searchBook(String name, String author) {
        return Observable.fromCallable(() -> isExistCAcheAndGetResultWithItemsList
                (CACHE_ID_WITH_ITEMS, name + author, bookRepository.searchBook(name, author)))
                .doOnNext(v -> isExistAndSubmitCacheWithItemsList(CACHE_ID_WITH_ITEMS, name + author, v))
                .map(Observable::from)
                .compose(Observable::merge)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Book> getByIdWithLibrary(UUID id) {
        return Observable.just(id)
                .map(bookRepository::getByIdWithLibraries)
                .filter(Objects::nonNull)
                .doOnNext(s -> isExistAndSubmitCache(CACHE_ID_WITH_ITEMS, s))
                .subscribeOn(Schedulers.io());
    }

    public Observable<Book> addLibrary(UUID bookId, UUID libraryId) {
        return Observable.fromCallable(() -> bookLibraryRepository.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setBookId(bookId)
                        .setLibraryId(libraryId))))
                .map(bl -> bookRepository.getByIdWithLibraries(bl.getId().getBookId()))
                .doOnNext(v -> hashOperations.put(CACHE_ID_WITH_ITEMS, bookId, v))
                .subscribeOn(Schedulers.io());
    }

}
