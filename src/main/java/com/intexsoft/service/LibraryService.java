package com.intexsoft.service;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.model.Library;
import com.intexsoft.repository.LibraryBookRepository;
import com.intexsoft.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Objects;
import java.util.UUID;

@Service
public class LibraryService extends CommonService<Library, UUID, Library> {

    @Autowired
    private LibraryBookRepository libraryBookRepository;

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
                .compose(observable -> Observable.from(libraryBookRepository.searchBooks(libraryId)))
                .subscribeOn(Schedulers.io());
    }

    public Observable<Library> addBook(BookLibraryId id) {
        return Observable.just(id)
                .map(s -> libraryBookRepository.save(new BookLibrary()
                        .setId(s)
                        .setVersion(1)
                        .setBook(new Book().setId(s.getBookId()))
                        .setLibrary(new Library().setId(s.getLibraryId()))))
                .map(BookLibrary::getLibrary)
                .subscribeOn(Schedulers.io());
    }

    @Override
    protected UUID getGeneratedId() {
        return UUID.randomUUID();
    }
}
