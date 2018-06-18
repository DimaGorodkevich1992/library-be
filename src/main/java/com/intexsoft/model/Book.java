package com.intexsoft.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "books")
public class Book extends CommonModel<UUID> {
    @Id
    @Column(name = "id")
    private UUID id;
    private String name;
    private Date published;
    private String author;
    @Column(name = "number_pages")
    private Integer numberPages;
    @Override
    public void setId(UUID uuid) {
        this.id = uuid;
    }
    @Override
    public UUID getId() {
        return id;
    }


}
