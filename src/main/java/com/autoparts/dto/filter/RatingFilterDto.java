package com.autoparts.dto.filter;

import com.autoparts.dto.rating.RatingItemDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
//@NoArgsConstructor
public class RatingFilterDto {

    private final String type = "rating";
    private final String slug = "rating";
    private final String name = "Рейтинг";
    List<RatingItemDto> items;
    List<Integer> value = new ArrayList<>();

    public RatingFilterDto(List<RatingItemDto> items, List<Integer> rating) {
        this.items = items;

        if (rating != null){
            this.value.addAll(rating);
        }
    }



}
