package com.intexsoft.controller.dtomapper;

import com.intexsoft.dto.LibraryDto;
import com.intexsoft.model.Library;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LibraryDtoMapper extends AbstractDtoMapper<Library, LibraryDto,UUID> {

    @Override
    protected void instructionToDto(Library entity, LibraryDto dto) {
        super.instructionToDto(entity, dto);
    }

    @Override
    protected Class<Library> getEntityClass() {
        return Library.class;
    }

    @Override
    protected Class<LibraryDto> getDtoClass() {
        return LibraryDto.class;
    }
}
