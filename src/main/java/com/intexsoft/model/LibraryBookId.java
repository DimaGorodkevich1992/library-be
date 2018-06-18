package com.intexsoft.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Embeddable
public class LibraryBookId implements Serializable {

    private static final long serialVersionUID = 2716468985254890805L;

    @Column(name = "book_id")
    private UUID bookId;
    @Column(name = "library_id")
    private UUID libraryId;

}
