package com.intexsoft.controller.dtomapper;

import com.intexsoft.dto.LibraryDtoWithBooks;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;
@Component
public class LibraryDtoMapperWithBooks extends AbstractDtoMapper<Library, LibraryDtoWithBooks, UUID> {

    @Autowired
    private BookDtoMapper bookDtoMapper;

    @Override
    protected void instructionToDto(Library entity, LibraryDtoWithBooks dto) {
        super.instructionToDto(entity, dto);
        dto.setBooks(entity.getBooks().stream()
                .map(BookLibrary::getBook)
                .map(bookDtoMapper::toDto)
                .collect(Collectors.toSet()));
    }

    @Override
    protected Class<Library> getEntityClass() {
        return Library.class;
    }

    @Override
    protected Class<LibraryDtoWithBooks> getDtoClass() {
        return LibraryDtoWithBooks.class;
    }
}
