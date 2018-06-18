package com.intexsoft.controller.dtomapper;


import com.intexsoft.dto.BookDto;
import com.intexsoft.model.Book;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class BookDtoMapper extends AbstractDtoMapper<Book, BookDto,UUID> {

    @Override
    protected void instructionToDto(Book entity, BookDto dto) {
        super.instructionToDto(entity, dto);
        dto.setInfo(entity.toString());
    }

    @Override
    protected void instructionToEntity(BookDto dto, Book entity) {
        super.instructionToEntity(dto, entity);
    }

    @Override
    protected Class<Book> getEntityClass() {
        return Book.class;
    }

    @Override
    protected Class<BookDto> getDtoClass() {
        return BookDto.class;
    }


}
