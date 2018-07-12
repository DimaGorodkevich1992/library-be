package com.intexsoft.service;

import com.intexsoft.model.Library;
import com.intexsoft.repository.BookLibraryRepository;
import com.intexsoft.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

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
        return /*Observable.fromCallable(() -> isExistCAcheAndGetResultWithItemsList
                (CACHE_ID_WITH_ITEMS, name + address, libraryRepository.searchLibrary(name, address)))
                .doOnNext(v -> isExistAndSubmitCacheWithItemsList(CACHE_ID_WITH_ITEMS, name + address, v))
                .map(Observable::from)
                .compose(Observable::merge)
                .subscribeOn(Schedulers.io());*/null;
    }

    public Observable<Library> getByIdWithBooks(UUID id) {
        return /*Observable.just(id)
                .map(s -> isExistCacheAndGetResult(CACHE_ID_WITH_ITEMS, s, libraryRepository.getByIdWithBooks(s)))
                .filter(Objects::nonNull)
                .doOnNext(s -> isExistAndSubmitCache(CACHE_ID_WITH_ITEMS, s))
                .subscribeOn(Schedulers.io());*/  null;
    }

    public Observable<Library> addBook(UUID libraryId, UUID bookId) {
        return /*Observable.fromCallable(() -> bookLibraryRepository.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setLibraryId(libraryId)
                        .setBookId(bookId))))
                .map(bl -> libraryRepository.getByIdWithBooks(bl.getId().getLibraryId()))
                .doOnNext(b -> hashOperations.put(CACHE_ID_WITH_ITEMS, b.getId(), b))
                .subscribeOn(Schedulers.io());*/ null;
    }

}
