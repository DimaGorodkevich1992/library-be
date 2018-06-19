package com.intexsoft.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;


@Data
@Accessors(chain = true)
@MappedSuperclass
public abstract class CommonModel<I, T extends CommonModel<I, T>> {

    @Version
    private long version;

    public abstract T setId(I id);

    public abstract I getId();
}
