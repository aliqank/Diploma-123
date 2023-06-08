package com.autoparts.mapper.product;

import com.autoparts.dto.product.ProductExcelDto;
import com.autoparts.entity.Category;
import com.autoparts.entity.Product;
import com.autoparts.repository.CategoryRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductExcelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    List<Product> toEntityList(List<ProductExcelDto> productExcelDto, @Context CategoryRepository categoryRepository);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category",source = "categoryId",qualifiedByName = "categoryFromCategoryName")
    Product toEntity(ProductExcelDto productExcelDto, @Context CategoryRepository categoryRepository);


    @Named("categoryFromCategoryName")
    default Category categoryFromCategoryName(String value, @Context CategoryRepository categoryRepository){
        return categoryRepository.findByName(value);
    }
}
