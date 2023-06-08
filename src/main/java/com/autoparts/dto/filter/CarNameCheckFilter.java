package com.autoparts.dto.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class CarNameCheckFilter {
    private final String type = "check";
    private final String slug = "car";
    private final String name = "Car";
    List<String> items;
    Set<String> value;
    public CarNameCheckFilter(List<String> items, Set<String> brands) {
        this.items = items;
        this.value = brands;
    }

    public Set<String> getCarNames() {
        return this.value;
    }

}

