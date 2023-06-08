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
public class TovarExcelDto {

    @ExcelCellName("Name")
    private String name;

    @ExcelCellName("Slug")
    private String slug;

    @ExcelCellName("Sku")
    private String sku;

    @ExcelCellName("Price")
    private Integer price;

    @ExcelCellName("ImageURL")
    private String images;

    @ExcelCellName("BrandName")
    private String brand;

    @ExcelCellName("Excerpt")
    private String excerpt;

    @ExcelCellName("Description")
    private String description;

    @ExcelCellName("CategoryName")
    private String category;

    @ExcelCellName("CarBrand")
    private String carBrand;

    @ExcelCellName("PartNumber")
    private String partNumber;

    @ExcelCellName("Quantity")
    private int quantity;

    @ExcelCellName("AttributesValue")
    private String attributesValue;


}
