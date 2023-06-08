package com.autoparts.mapper.tovar;

import com.autoparts.dto.order.TovarReadDto;
import com.autoparts.dto.tovar.TovarUpdateDto;
import com.autoparts.entity.ProductImage;
import com.autoparts.entity.Tovar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TovarReadMapper {
    TovarReadDto toDto(Tovar tovar);

    default List<String> map(List<ProductImage> value){
        return value.stream()
                .map(ProductImage::getName)
                .collect(Collectors.toList());
    }


    @Mapping(target = "images", ignore = true)
    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Tovar entity, TovarReadDto updateEntity);

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Tovar entity, TovarUpdateDto updateEntity);
}