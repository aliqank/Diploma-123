package com.autoparts.dto.filter;

import com.autoparts.entity.Brand;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class Check {
    private final String type = "check";
    private final String slug = "brand";
    private final String name = "Бренд";
    List<Brand> items;
    Set<String> value;
    public Check(List<Brand> items, Set<String> brands) {
        this.items = items;
        this.value = brands;
    }

    public Set<String> getBrandName() {
        return this.value;
    }

}

