package com.intexsoft.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "libraries")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Library extends CommonModel<UUID,Library> {
    @Id
    @Column(name = "id")
    private UUID id;
    private String name;
    private String address;

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
