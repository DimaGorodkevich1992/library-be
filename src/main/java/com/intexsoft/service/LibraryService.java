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

import java.util.Objects;
import java.util.UUID;

@Service
public class LibraryService extends CommonService<Library, UUID> {

    private static final String CACHE_ID_WITH_ITEMS = "libraries";

    @Autowired
    private BookLibraryRepository bookLibraryRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Override
    protected Class<Library> getModelClass() {
        return Library.class;
    }

    @Override
    protected Class<UUID> getIdClass() {
        return UUID.class;
    }

    @Override
    protected String searchCacheId() {
        return "searchLibraries";
    }

    @Override
    protected String withItemsCacheId() {
        return "libraryWithBooks";
    }

    @Override
    protected String commonCacheId() {
        return "commonLibraries";
    }

    public Observable<Library> searchLibrary(String name, String address) {
        return Observable.fromCallable(() -> libraryRepository.searchLibrary(name, address))
                .flatMap(Observable::from)
                .compose(cacheRx.cachable(searchCacheId(), name + address))
                .subscribeOn(Schedulers.io());
    }

    public Observable<Library> getByIdWithBooks(UUID id) {
        return Observable.just(id)
                .map(s -> libraryRepository.getByIdWithBooks(s))
                .compose(cacheRx.cachable(withItemsCacheId(), id))
                .filter(Objects::nonNull)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Library> addBook(UUID libraryId, UUID bookId) {
        return Observable.fromCallable(() -> bookLibraryRepository.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setLibraryId(libraryId)
                        .setBookId(bookId))))
                .compose(cacheRx.cachePut(withItemsCacheId(), bookId))
                .compose(cacheRx.cacheDelete(withItemsCacheId(), bookId))
                .map(bl -> libraryRepository.getByIdWithBooks(bl.getId().getLibraryId()))
                .subscribeOn(Schedulers.io());
    }

}
