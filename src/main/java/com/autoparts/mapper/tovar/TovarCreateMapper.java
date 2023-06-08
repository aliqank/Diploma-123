package com.autoparts.mapper.tovar;

import com.autoparts.dto.tovar.TovarCreateDto;
import com.autoparts.dto.tovar.TovarCreateEditDto;
import com.autoparts.entity.ProductImage;
import com.autoparts.entity.Tovar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TovarCreateMapper {

    @Mapping(target = "brand", ignore = true)
    Tovar toEntity(TovarCreateDto tovarCreateDto);

    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "category", ignore = true)
    Tovar toEntity(TovarCreateEditDto tovarCreateEditDto);
    ProductImage toProductImageEntity(TovarCreateDto dto);


}
