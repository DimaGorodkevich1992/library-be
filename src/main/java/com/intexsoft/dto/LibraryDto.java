package com.intexsoft.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class LibraryDto extends CommonDto<UUID> {
    private String name;
    private String address;

}
