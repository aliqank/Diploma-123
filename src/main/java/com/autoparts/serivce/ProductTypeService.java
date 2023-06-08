package com.autoparts.serivce;

import com.autoparts.dto.attribute.ProductTypeDto;
import com.autoparts.mapper.product.ProductTypeMapper;
import com.autoparts.repository.ProductTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProductTypeService {

    private final ProductTypeRepository productTypeRepository;
    private final ProductTypeMapper typeMapper;

    public List<ProductTypeDto> findAll(){

        return productTypeRepository.findAll().stream()
                .map(typeMapper::map)
                .toList();

    }
}
