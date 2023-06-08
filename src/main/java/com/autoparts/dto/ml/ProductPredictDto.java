package com.autoparts.dto.ml;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPredictDto {
    @JsonProperty("Id")
    private Long id;

    @JsonProperty("Date")
    private LocalDate date;

    @JsonProperty("CountOfSells")
    private Long countOfSells;

    @JsonProperty("Price")
    private Long price;

    @JsonProperty("Brand ")
    private long brand;

//    @JsonIgnore
//    @JsonProperty("Category")
//    private long category;
//
//    @JsonIgnore
//    @JsonProperty("CarBrand")
//    private String carBrand;

}
