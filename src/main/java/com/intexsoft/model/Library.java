package com.intexsoft.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "libraries")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Library extends CommonModel<UUID> {
    @Id
    @Column(name = "id")
    private UUID id;
    private String name;
    private String address;
    @Override
    public void setId(UUID uuid) {
        this.id = uuid;
    }
    @Override
    public UUID getId() {
        return id;
    }



}
