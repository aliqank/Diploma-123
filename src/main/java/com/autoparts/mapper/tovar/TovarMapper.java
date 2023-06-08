package com.autoparts.mapper.tovar;

import com.autoparts.dto.tovar.TovarDto;
import com.autoparts.entity.ProductAttribute;
import com.autoparts.entity.ProductImage;
import com.autoparts.entity.Tovar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TovarMapper {


    @Mapping(target = "categories", source = "category")
    @Mapping(target = "brandName", source = "brand.name")
    TovarDto toDto(Tovar tovar);
    default List<String> map(List<ProductImage> value){
        return value.stream()
                .map(ProductImage::getName)
                .collect(Collectors.toList());
    }
//    default List<String> map(List<ProductImage> value){
//       return value.stream()
//                .map(ProductImage::getName)
//                .toList();
//    }

    default Set<String> mapAtt(Set<ProductAttribute> value){
        return value.stream().map(ProductAttribute::getSlug)
                .collect(Collectors.toSet());
    }


}
