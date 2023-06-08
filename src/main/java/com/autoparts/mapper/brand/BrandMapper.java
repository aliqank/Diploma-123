package com.autoparts.mapper.brand;

import com.autoparts.dto.brand.BrandDto;
import com.autoparts.dto.excel.BrandExcelDto;
import com.autoparts.dto.filter.BrandFilterDto;
import com.autoparts.dto.filter.Check;
import com.autoparts.entity.Brand;
import com.autoparts.repository.TovarRepository;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface BrandMapper {


    @Mapping(target = "items", ignore = true)
    Check toDto(Brand brand, @Context TovarRepository tovarRepository);

    default List<BrandFilterDto> map(Brand value, @Context TovarRepository tovarRepository){
        int i = tovarRepository.countTovarByBrand(value);
        BrandFilterDto brandFilterDto = BrandFilterDto.builder()
                .name(value.getName())
                .slug(value.getSlug())
                .count(i)
                .build();

        List<BrandFilterDto> objects = new ArrayList<>();
        objects.add(brandFilterDto);
        return objects;
    }

    BrandDto toDto(Brand brand);

    @Mapping(target = "id", ignore = true)
    Brand toEntity(BrandDto brandDto);

    List<Brand> toList(List<BrandExcelDto> brandExcelDtos);
}
