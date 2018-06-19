package com.intexsoft.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "books")
public class Book extends CommonModel<UUID, Book> {
    @Id
    @Column(name = "id")
    private UUID id;
    private String name;
    private Date published;
    private String author;
    @Column(name = "number_pages")
    private Integer numberPages;
    @OneToMany(mappedBy = "book")
    private Set<LibraryBook> libraries = new HashSet<>();

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Book setId(UUID id) {
        this.id = id;
        return this;
    }
}
