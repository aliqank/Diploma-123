package com.autoparts.mapper.product;

import com.autoparts.dto.product.ProductDto;
import com.autoparts.entity.Category;
import com.autoparts.entity.Product;
import com.autoparts.mapper.category.CategoryMapper;
import com.autoparts.repository.CategoryRepository;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Mapper(componentModel = "spring",uses = {CategoryRepository.class, CategoryMapper.class})
public interface ProductMapper {
    @Mapping(target = "image", ignore = true)
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "categoryId", target = "category", qualifiedByName = "mapLongToCategory")
    Product toEntity(ProductDto productDto);

    @Mapping(target = "categoryId", source = "category.id",ignore = true)
    List<ProductDto> toDtoList(List<Product> product);

    @Mapping(target = "id", source = "categoryId")
    List<Product> toEntityList(List<ProductDto> productDto);

    @Named("mapLongToCategory")
    default Category mapLongToCategory(Long value){
        return Category.builder()
                .id(value)
                .build();
    }

    default String map(MultipartFile file){
        return file.getOriginalFilename();
    }

}

