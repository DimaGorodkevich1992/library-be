package com.intexsoft.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"book", "library"})
@Accessors(chain = true)
@Entity
@Table(name = "books_libraries")
public class BookLibrary extends CommonModel<BookLibraryId, BookLibrary> implements Serializable {

    private static final long serialVersionUID = -3685625836502789462L;

    @Override
    protected BookLibrary getModel() {
        return this;
    }

    @EmbeddedId
    private BookLibraryId id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", insertable = false, updatable = false)
    private Library library;

}
