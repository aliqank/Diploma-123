package com.autoparts.dto.filter;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class ColorFilter {
    private final String type = "color";
    private final String slug = "brand";
    private final String name = "Brand";

}
