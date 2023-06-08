package com.autoparts.serivce;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExcelGeneratorService {

    private <T> List<String> objectFieldsToValues(T object) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return Arrays.stream(fields)
                .map(field -> {
                    try {
                        Object value = field.get(object);
                        if (value != null) {
                            if (!isPrimitiveOrWrapper(value.getClass())) {
                                Field[] subFields = value.getClass().getDeclaredFields();
                                for (Field subField : subFields) {
                                    subField.setAccessible(true);
                                }
                                return Arrays.stream(subFields)
                                        .map(subField -> {
                                            try {
                                                Object subValue = subField.get(value);
                                                return subValue != null ? subValue.toString() : "";
                                            } catch (IllegalAccessException e) {
                                                throw new RuntimeException(e);
                                            }
                                        })
                                        .collect(Collectors.joining(", "));
                            }
                            return value.toString();
                        } else {
                            return "";
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == Double.class || clazz == Float.class || clazz == Long.class ||
                clazz == Integer.class || clazz == Short.class || clazz == Character.class || clazz == Byte.class ||
                clazz == Boolean.class || clazz == String.class || clazz == java.sql.Timestamp.class;
    }



    public <T> ByteArrayInputStream listToExcelFile(List<T> objects) {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Objects");

            Row headerRow = sheet.createRow(0);
            Field[] fields = objects.get(0).getClass().getDeclaredFields();
            IntStream.range(0, fields.length)
                    .forEach(i -> headerRow.createCell(i).setCellValue(fields[i].getName()));

            IntStream.range(0, objects.size())
                    .forEach(i -> {
                        try {
                            List<String> values = objectFieldsToValues(objects.get(i));
                            Row dataRow = sheet.createRow(i + 1);
                            IntStream.range(0, values.size())
                                    .forEach(j -> dataRow.createCell(j).setCellValue(values.get(j)));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}
