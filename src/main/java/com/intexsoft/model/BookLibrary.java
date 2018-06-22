package com.intexsoft.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"book", "library"})
@Accessors(chain = true)
@Entity
@Table(name = "books_libraries")
public class BookLibrary extends CommonModel<BookLibraryId, BookLibrary> {

    @Override
    protected BookLibrary getModel() {
        return this;
    }

    @EmbeddedId
    private BookLibraryId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", insertable = false, updatable = false)
    private Library library;

}
