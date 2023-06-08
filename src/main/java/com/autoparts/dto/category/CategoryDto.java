package com.autoparts.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    Long id;
    String name;
    String slug;
    String type;
    String image;
    Integer items;
    String layout;
    List<CategorySubDto> children = new ArrayList<>();
}
