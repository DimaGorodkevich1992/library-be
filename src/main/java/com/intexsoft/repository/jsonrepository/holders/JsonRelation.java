package com.intexsoft.repository.jsonrepository.holders;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class JsonRelation<R extends Serializable> {

    private R leftEntityId;
    private R rightEntityId;

}
