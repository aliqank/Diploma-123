package com.autoparts.dto.excel;

import com.poiji.annotation.ExcelCellName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandExcelDto {

    @ExcelCellName("Name")
    private String name;

    @ExcelCellName("Slug")
    private String slug;

    @ExcelCellName("ImageURL")
    private String image;

    @ExcelCellName("Country")
    private String country;

}
