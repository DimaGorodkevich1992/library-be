package com.intexsoft.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookDto extends CommonDto<UUID>{
    private String name;
    private Date published;
    private String author;
    private Integer numberPages;
    private String info ;

}