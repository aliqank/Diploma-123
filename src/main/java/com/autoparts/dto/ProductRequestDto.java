package com.autoparts.dto;

import com.autoparts.dto.tovar.TovarCreateEditDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder()
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    TovarCreateEditDto product;
    MultipartFile file;
}
