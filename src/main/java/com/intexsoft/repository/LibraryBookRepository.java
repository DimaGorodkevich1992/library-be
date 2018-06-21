package com.intexsoft.repository;

import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.BookLibraryId;
import com.intexsoft.model.Library;

import java.util.List;
import java.util.UUID;

public interface LibraryBookRepository extends CommonRepository<BookLibrary, BookLibraryId> {

    List<Book> searchBooks(UUID libraryId);

    List<Library> searchLibraries(UUID bookId);

}
