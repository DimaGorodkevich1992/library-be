package com.intexsoft.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "books")
public class Book extends CommonModel<UUID, Book> implements Serializable {

    private static final long serialVersionUID = -4037997523824934833L;

    @Id
    @Column(name = "id")
    private UUID id;
    private String name;
    private Date published;
    private String author;
    @Column(name = "number_pages")
    private Integer numberPages;
    @OneToMany(mappedBy = "book",cascade = CascadeType.REMOVE)
    private Set<BookLibrary> libraries = new HashSet<>();

    @Override
    protected Book getModel() {
        return this;
    }

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
