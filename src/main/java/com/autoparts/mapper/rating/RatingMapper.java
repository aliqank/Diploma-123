package com.autoparts.mapper.rating;

import com.autoparts.dto.rating.RatingItemDto;
import com.autoparts.entity.Review;
import com.autoparts.mapper.category.CategoryMapper;
import com.autoparts.repository.CategoryRepository;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring",uses = {CategoryRepository.class, CategoryMapper.class})
public interface RatingMapper {

    RatingItemDto toDto(Review review);

}

