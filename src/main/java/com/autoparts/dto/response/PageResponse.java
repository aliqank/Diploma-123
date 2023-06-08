package com.autoparts.dto.response;

import com.autoparts.dto.filter.*;
import com.autoparts.dto.rating.RatingItemDto;
import com.autoparts.serivce.BrandService;
import com.autoparts.serivce.CategoryService;
import com.autoparts.serivce.TovarService;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.*;

@Value
public class PageResponse<T> {

    List<T> items;
    int page;
    int limit;
    int total;
    int pages;
    int from;
    int to;
    String sort;
    List<Object> filters;

    public static <T> PageResponse<T> of(Page<T> pageResponse,
                                         ProductFilter filter,
                                         CategoryService categoryService,
                                         BrandService brandService,
                                         TovarService tovarService) {

        int page = pageResponse.getNumber() + 1;
        int limit = Optional.ofNullable(filter.size()).orElse(20);
        int total = (int) pageResponse.getTotalElements();
        int pages = pageResponse.getTotalPages();
        int from = (page - 1) * limit;
        int to = Math.min(from + limit, total);

        String sort = Optional.ofNullable(filter.sort()).orElse("");

        List<Object> filters = new ArrayList<>();
        filters.add(new CategoryFilterDto(categoryService.findAllCategories()));
        filters.add(new Check(brandService.findAll(), new HashSet<>()));

        PriceFilterDto priceFilterDto = new PriceFilterDto(filter.min(), filter.max());
        filters.add(priceFilterDto);

        List<RatingItemDto> ratingItems = tovarService.getRatingAndCount();
        RatingFilterDto ratingFilterDto = new RatingFilterDto(ratingItems, filter.rating());
        filters.add(ratingFilterDto);

        if (filter.brand() != null) {
            Arrays.stream(filter.brand().split(","))
                    .filter(name -> !name.isEmpty())
                    .forEach(((Check) filters.get(1)).getBrandName()::add);
        }


        return new PageResponse<>(pageResponse.getContent(), page, limit, total, pages, from, to, sort, filters);
    }



}
