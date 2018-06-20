package com.intexsoft.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookDtoWithLibraries extends BookDto {

    private Set<LibraryDtoInfo> libraries;
}
