package com.intexsoft.repository;

import com.intexsoft.model.Book;

import java.util.List;
import java.util.UUID;


public interface BookRepository extends CommonRepository<Book,UUID>{


    List<Book> searchBook(String name, String author);



}
