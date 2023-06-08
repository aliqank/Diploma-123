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
public class TovarCountExcel {

    @ExcelCellName("Name")
    private String name;

    @ExcelCellName("Quantity")
    private int quantity;
}
