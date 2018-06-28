package com.intexsoft.repository.jsonrepository.holders;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class JsonRelationID<R1 extends Serializable, R2 extends Serializable> {

    private R1 leftEntityId;
    private R2 rightEntityId;

}
