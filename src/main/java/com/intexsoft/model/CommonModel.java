package com.intexsoft.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;


@Data
@EqualsAndHashCode
@Accessors(chain = true)
@MappedSuperclass
public abstract class CommonModel<I> {

    public abstract void setId(I i);
    public abstract I getId();
    @Version
    private long version;
}
