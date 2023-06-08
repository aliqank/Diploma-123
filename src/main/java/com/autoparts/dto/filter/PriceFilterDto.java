package com.autoparts.dto.filter;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//@NoArgsConstructor
@Data
public class PriceFilterDto {

    private final String type = "range";
    private final String slug = "price";
    private final String name = "Цена";
    private final int min = 0;
    private final int max = 500_000;
    private List<String> value = new ArrayList<>();

    public PriceFilterDto(Integer priceMin, Integer priceMax) {
        if (priceMin != null && priceMax != null) {
            this.value.add(0, priceMin.toString());
            this.value.add(1, priceMax.toString());
        } else {
            this.value.add(0, Integer.toString(min));
            this.value.add(1, Integer.toString(max));
        }
    }
}
