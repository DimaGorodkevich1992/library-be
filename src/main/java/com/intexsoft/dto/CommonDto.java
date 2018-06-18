package com.intexsoft.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class CommonDto<I>  {
    private I id ;
    private long version;
}
