package com.intexsoft.controller.dtomapper;

import com.intexsoft.dto.BookDtoInfo;
import com.intexsoft.dto.LibraryDtoWithBooks;
import com.intexsoft.model.BookLibrary;
import com.intexsoft.model.Library;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;
@Component
public class LibraryDtoMapperWithBooks extends AbstractDtoMapper<Library, LibraryDtoWithBooks, UUID, Library> {

    @Override
    protected void instructionToDto(Library entity, LibraryDtoWithBooks dto) {
        super.instructionToDto(entity, dto);
        dto.setBooks(entity.getBooks().stream()
                .map(BookLibrary::getBook)
                .map(b -> new BookDtoInfo()
                        .setAuthor(b.getAuthor())
                        .setName(b.getName())
                        .setNumberPages(b.getNumberPages())
                        .setPublished(b.getPublished()))
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
