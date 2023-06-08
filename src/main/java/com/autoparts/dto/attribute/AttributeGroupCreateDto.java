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
public class AttributeGroupCreateDto {

    String name;
    String slug;
    Long typeId;
    Set<AttributeCreateDto> attributes;
}
