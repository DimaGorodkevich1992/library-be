package com.intexsoft.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@MappedSuperclass
public abstract class CommonModel<I extends Serializable, T extends CommonModel<I, T>> {

    @Version
    private long version;

    protected abstract T getModel();

    public long getVersion() {
        return version;
    }

    public T setVersion(long version) {
        this.version = version;
        return getModel();
    }

    public abstract T setId(I id);

    public abstract I getId();
}
