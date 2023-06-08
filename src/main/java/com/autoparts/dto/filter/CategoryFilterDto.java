package com.autoparts.dto.filter;

import com.autoparts.dto.category.CategoryDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class CategoryFilterDto {

    private final String type = "category";
    private final String slug = "category";
    private final String name = "Категория";
    private List<CategoryDto> items;

    public CategoryFilterDto(List<CategoryDto> items) {
        this.items = items;
    }
}
