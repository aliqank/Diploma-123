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
public class ProductTypeDto {

    String slug;
    String name;
    Set<AttributeGroupDto> attributeGroups;
}
