package com.intexsoft.repository;


import com.intexsoft.model.Library;

import java.util.List;
import java.util.UUID;

public interface LibraryRepository extends CommonRepository<Library,UUID> {

    List<Library> searchLibrary(String name,String address);
}
