package com.intexsoft.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "books_libraries")
public class LibraryBook extends CommonModel<LibraryBookId> {
    @EmbeddedId
    private LibraryBookId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", insertable = false, updatable = false)
    private Library library;

    @Override
    public void setId(LibraryBookId libraryBookId) {
        this.id = libraryBookId;
    }

    @Override
    public LibraryBookId getId() {
        return id;
    }
}
