package com.autoparts.dto.attribute;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttributeDto {

    String name;
    String slug;
    List<String> attributes;

}
