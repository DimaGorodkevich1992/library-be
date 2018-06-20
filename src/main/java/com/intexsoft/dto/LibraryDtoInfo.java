package com.intexsoft.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LibraryDtoInfo {
    private String name;
    private String address;
}
