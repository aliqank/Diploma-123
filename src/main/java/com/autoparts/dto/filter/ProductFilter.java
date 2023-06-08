package com.autoparts.dto.filter;

import java.util.List;

public record ProductFilter (String category,
                             String price,
                             Integer min,
                             Integer max,
                             String brand,
                             String sort,
                             Integer size,
                             String car,
                             List<Integer> rating){
}
