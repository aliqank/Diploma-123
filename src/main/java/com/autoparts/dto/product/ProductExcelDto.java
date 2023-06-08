package com.autoparts.dto.product;

import com.poiji.annotation.ExcelCellName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductExcelDto {

    @ExcelCellName("Бренд")
    String brand;
    @ExcelCellName("Артикул")
    String articleNo;
    @ExcelCellName("Наименование")
    String name;
    @ExcelCellName("Кол-во")
    int count;
    @ExcelCellName("Цена")
    BigDecimal price;
    @ExcelCellName("Машины")
    String carName;
    @ExcelCellName("Категория")
    String categoryId;
    @ExcelCellName("Картинка")
    String image;
}
