package com.intexsoft.repository;

import com.intexsoft.model.Book;
import com.intexsoft.model.Library;
import com.intexsoft.model.LibraryBook;
import com.intexsoft.model.LibraryBookId;

import java.util.List;
import java.util.UUID;

public interface LibraryBookRepository extends CommonRepository<LibraryBook, LibraryBookId> {

    List<Book> searchBooks(UUID libraryId);

    List<Library> searchLibraries(UUID bookId);


}
