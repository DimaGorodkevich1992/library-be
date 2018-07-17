package com.intexsoft.service;

import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.model.Library;
import com.intexsoft.repository.BookLibraryRepository;
import com.intexsoft.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.schedulers.Schedulers;

import javax.annotation.PreDestroy;
import java.util.Objects;
import java.util.UUID;

@Service
public class LibraryService extends CommonService<Library, UUID> {

    @Autowired
    private BookLibraryRepository bookLibraryRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private BookService bookService;

    @PreDestroy
    private void cleanCache() {
        cacheRx.cleanCache(searchCacheId());
        cacheRx.cleanCache(entityWithItemsCacheId());
        cacheRx.cleanCache(entityCacheId());
    }

    @Override
    protected String searchCacheId() {
        return "searchLibraries";
    }

    @Override
    protected String entityWithItemsCacheId() {
        return "libraryWithBooks";
    }

    @Override
    protected String entityCacheId() {
        return "commonLibraries";
    }

    public Observable<Library> searchLibrary(String name, String address) {
        return Observable.fromCallable(() -> libraryRepository.searchLibrary(name, address))
                .flatMap(Observable::from)
                .compose(cacheRx.cachable(searchCacheId(), name, address))
                .subscribeOn(Schedulers.io());
    }

    public Observable<Library> getByIdWithBooks(UUID id) {
        return Observable.just(id)
                .map(s -> libraryRepository.getByIdWithBooks(s))
                .compose(cacheRx.cachable(entityWithItemsCacheId(), id))
                .filter(Objects::nonNull)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Library> addBook(UUID libraryId, UUID bookId) {
        return Observable.fromCallable(() -> bookLibraryRepository.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setLibraryId(libraryId)
                        .setBookId(bookId))))
                .map(bl -> libraryRepository.getByIdWithBooks(bl.getId().getLibraryId()))
                .compose(cacheRx.cachePut(entityWithItemsCacheId(), Library::getId))
                .compose(cacheRx.cachePut(bookService.searchCacheId(), s -> bookId))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Library> update(Library library) {
        return super.update(library)
                .compose(cacheRx.cacheDeleteAll(bookService.entityWithItemsCacheId()))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<UUID> delete(UUID id) {
        return super.delete(id)
                .compose(cacheRx.cacheDeleteAll(bookService.entityWithItemsCacheId()))
                .subscribeOn(Schedulers.io());
    }
}
