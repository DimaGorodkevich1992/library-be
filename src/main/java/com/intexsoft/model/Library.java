package com.intexsoft.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "libraries")
public class Library extends CommonModel<UUID, Library> {
    @Id
    @Column(name = "id")
    private UUID id;
    private String name;
    private String address;

    @Override
    protected Library getModel() {
        return this;
    }

    @OneToMany(mappedBy = "library")
    private Set<BookLibrary> books = new HashSet<>();

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Library setId(UUID id) {
        this.id = id;
        return this;
    }


}
