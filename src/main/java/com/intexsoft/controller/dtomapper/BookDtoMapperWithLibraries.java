package com.intexsoft.controller.dtomapper;

import com.intexsoft.dto.BookDtoWithLibraries;
import com.intexsoft.dto.LibraryDtoInfo;
import com.intexsoft.model.Book;
import com.intexsoft.model.BookLibrary;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BookDtoMapperWithLibraries extends AbstractDtoMapper<Book, BookDtoWithLibraries, UUID, Book> {

    @Override
    protected void instructionToDto(Book entity, BookDtoWithLibraries dto) {
        super.instructionToDto(entity, dto);
        dto.setInfo(entity.getAuthor() + "-" + entity.getName());
        dto.setLibraries(entity.getLibraries().stream()
                .map(BookLibrary::getLibrary)
                .map(l -> new LibraryDtoInfo()
                        .setName(l.getName())
                        .setAddress(l.getAddress()))
                .collect(Collectors.toSet()));
    }

    @Override
    protected Class<Book> getEntityClass() {

        return Book.class;
    }

    @Override
    protected Class<BookDtoWithLibraries> getDtoClass() {
        return BookDtoWithLibraries.class;
    }
}
