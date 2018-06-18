package com.intexsoft.repository.jsonrepository;

import com.intexsoft.model.Book;
import com.intexsoft.repository.BookRepository;
import com.intexsoft.repository.jsonrepository.holders.JsonDataHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "local", matchIfMissing = false)
public class JsonBookRepository extends JsonCommonRepository<Book, UUID> implements BookRepository {

    @Autowired
    private JsonDataHolder jsonDataHolder;


    @Override
    protected List<Book> getData() {
        return jsonDataHolder.getJsonData().getBookList();
    }


    @Override
    public List<Book> searchBook(String name, String author) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        searchCriterias.put("author", author);
        return search(searchCriterias);
    }


}