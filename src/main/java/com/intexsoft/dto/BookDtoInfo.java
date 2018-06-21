package com.intexsoft.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class BookDtoInfo {
    private String name;
    private Date published;
    private String author;
    private Integer numberPages;
}
