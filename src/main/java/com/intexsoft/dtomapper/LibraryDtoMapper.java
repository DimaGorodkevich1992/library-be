package com.intexsoft.dtomapper;

import com.intexsoft.dto.LibraryDto;
import com.intexsoft.model.Library;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface LibraryDtoMapper {

    LibraryDto toDto(Library library);

    List<LibraryDto> toDto(List<Library> bookLibraries);

    Set<LibraryDto> toDto(Set<Library> bookLibrarySet);

    Library fromDto(LibraryDto libraryDto);
}
