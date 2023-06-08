package com.autoparts.dto.charts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DatasetDto {

    private String label;
    private List<String> data;
    private String backgroundColor;
}
