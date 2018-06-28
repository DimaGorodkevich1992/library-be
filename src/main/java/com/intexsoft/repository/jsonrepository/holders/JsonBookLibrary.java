package com.intexsoft.repository.jsonrepository.holders;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class JsonBookLibrary extends JsonRelationID<UUID, UUID> {

}
