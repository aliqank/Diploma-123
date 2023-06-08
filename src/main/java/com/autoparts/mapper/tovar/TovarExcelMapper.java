package com.autoparts.mapper.tovar;

import com.autoparts.dto.excel.TovarExcelDto;
import com.autoparts.entity.Brand;
import com.autoparts.entity.Category;
import com.autoparts.entity.ProductImage;
import com.autoparts.entity.Tovar;
import com.autoparts.repository.BrandRepository;
import com.autoparts.repository.CategoryRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TovarExcelMapper {

    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryFromCategoryName")
    @Mapping(target = "brand", source = "brand", qualifiedByName = "brandFromBrandName")
    Tovar toEntity(TovarExcelDto tovarExcelDto, @Context CategoryRepository categoryRepository, @Context BrandRepository brandRepository);

    default List<Tovar> toEntityList(List<TovarExcelDto> tovarExcelDtoList, @Context CategoryRepository categoryRepository, @Context BrandRepository brandRepository) {
        List<Tovar> tovars = new ArrayList<>();
        for (TovarExcelDto tovarExcelDto : tovarExcelDtoList) {
            tovars.add(toEntity(tovarExcelDto, categoryRepository, brandRepository));
        }
        return tovars;
    }

    @Named("categoryFromCategoryName")
    default Category categoryFromCategoryName(String value, @Context CategoryRepository categoryRepository) {
        return categoryRepository.findByName(value);
    }

    @Named("brandFromBrandName")
    default Brand brandFromBrandName(String value, @Context BrandRepository brandRepository) {
        return brandRepository.findByName(value).orElseThrow( () -> new RuntimeException(value + " not found"));
    }

    default List<ProductImage> map(String value) {
        ProductImage build = ProductImage.builder()
                .name(value)
                .build();
        ArrayList<ProductImage> productImages = new ArrayList<>();
        productImages.add(build);
        return productImages;
    }
}
