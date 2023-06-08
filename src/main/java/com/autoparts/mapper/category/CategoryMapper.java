package com.autoparts.mapper.category;

import com.autoparts.dto.category.CategoryDto;
import com.autoparts.dto.excel.CategoryExcelDto;
import com.autoparts.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {


    @BeanMapping(qualifiedByName = "descOrder")
    CategoryDto toDto(Category category);

    List<CategoryDto> toDtoList(List<Category> category);

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryDto categoryDto);

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "layout", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "id", ignore = true)
    List<Category> toEntityList(List<CategoryExcelDto> categoryExcelDto);



}
