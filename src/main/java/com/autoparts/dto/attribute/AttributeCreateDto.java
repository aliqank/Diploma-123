package com.autoparts.dto.attribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttributeCreateDto {

    String name;
    String slug;
    Boolean featured;
    Long group_id;
    Long tovar_id;
    Set<AttributeValueCreateDto> values;

}
