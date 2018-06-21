package com.intexsoft.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class LibraryDto extends CommonDto<UUID> {

    private String name;
    private String address;

}
