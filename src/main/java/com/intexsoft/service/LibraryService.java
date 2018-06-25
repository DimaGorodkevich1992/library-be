package com.intexsoft.service;

import com.intexsoft.model.Book;
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

    @Autowired
    private BookLibraryRepository bookLibraryRepository;

    @Autowired
    private LibraryRepository repositoryLibrary;

    public Observable<Library> searchLibrary(String name, String address) {
        return Observable.fromCallable(() -> Observable.from(repositoryLibrary.searchLibrary(name, address)))
                .compose(Observable::merge)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Library> getByIdWithBooks(UUID id) {
        return Observable.just(id)
                .map(repositoryLibrary::getByIdWithBooks)
                .filter(Objects::nonNull)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Book> searchBooks(UUID libraryId) {
        return Observable.just(libraryId)
                .compose(observable -> Observable.from(bookLibraryRepository.searchBooks(libraryId)))
                .subscribeOn(Schedulers.io());
    }

    public Observable<Library> addBook(UUID libraryId, UUID bookId) {
        return Observable.fromCallable(() -> bookLibraryRepository.save(new BookLibrary()
                .setId(new BookLibraryId()
                        .setLibraryId(libraryId)
                        .setBookId(bookId))))
                .map(bl -> repositoryLibrary.getByIdWithBooks(bl.getId().getLibraryId()))
                .subscribeOn(Schedulers.io());
    }

}
