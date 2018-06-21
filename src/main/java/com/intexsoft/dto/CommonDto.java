package com.intexsoft.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class CommonDto<I extends Serializable> {

    private I id;
    private long version;
}
