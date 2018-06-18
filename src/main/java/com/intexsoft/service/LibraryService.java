package com.intexsoft.service;

import com.intexsoft.model.Book;
import com.intexsoft.model.Library;
import com.intexsoft.model.LibraryBook;
import com.intexsoft.repository.LibraryBookRepository;
import com.intexsoft.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.UUID;

@Service
public class LibraryService extends CommonService<Library, UUID> {

    @Autowired
    private LibraryBookRepository libraryBookRepository;

    @Autowired
    private LibraryRepository repositoryLibrary;

    public Observable<Library> searchLibrary(String name, String address) {  //todo
        return Observable.from(repositoryLibrary.searchLibrary(name,address))
                .subscribeOn(Schedulers.io());
    }

    public Observable<Book> searchBooks(UUID libraryId){
        return Observable.from(libraryBookRepository.searchBooks(libraryId))
                .subscribeOn(Schedulers.io());
    }

    @Override
    protected UUID getGeneratedId() {
        return UUID.randomUUID();
    }
}
