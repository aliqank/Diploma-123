package com.autoparts.dto.response;


import com.autoparts.dto.category.CategoryDto;
import com.autoparts.dto.filter.SearchFilter;
import com.autoparts.dto.tovar.TovarDto;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
public class SearchResponse {

    List<TovarDto> products;
    List<CategoryDto> categories;

    public static SearchResponse of(SearchFilter filter, Page<TovarDto> tovarResponse, Page<CategoryDto> categoryResponse){

        return new SearchResponse(tovarResponse.getContent(),categoryResponse.getContent());
    }
}
