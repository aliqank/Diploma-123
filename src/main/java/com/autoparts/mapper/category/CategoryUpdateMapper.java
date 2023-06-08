package com.autoparts.mapper.category;

import com.autoparts.dto.category.CategoryUpdateDto;
import com.autoparts.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryUpdateMapper {


    @Mapping(target = "type", ignore = true)
    @Mapping(target = "layout", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "children", ignore = true)
    void update(@MappingTarget Category entity, CategoryUpdateDto updateEntity);

}
