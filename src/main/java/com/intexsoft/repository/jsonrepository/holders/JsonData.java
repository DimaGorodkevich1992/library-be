package com.intexsoft.repository.jsonrepository.holders;

import com.intexsoft.model.Book;
import com.intexsoft.model.Library;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class JsonData {

    private List<Book> bookList;
    private List<Library> libraryList;

}
