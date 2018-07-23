package com.intexsoft.dtomapper;

import com.intexsoft.dto.BookDto;
import com.intexsoft.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BookDtoMapper {

    @Mapping( expression = "java(book.getName() + book.getAuthor())" , target = "info")
    BookDto toDto(Book book);

    List<BookDto> toDto(List<Book> bookLibraries);

    Set<BookDto> toDto(Set<Book> bookLibraries);

    Book fromDto(BookDto bookDto);

}
