package com.intexsoft.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class LibraryDtoWithBooks extends LibraryDto {
    private Set<BookDtoInfo> books;
}
