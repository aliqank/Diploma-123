package com.autoparts.serivce;

import com.autoparts.dto.attribute.AttributeCreateDto;
import com.autoparts.entity.ProductAttribute;
import com.autoparts.entity.ProductAttributeValue;
import com.autoparts.mapper.attribute.AttributeMapper;
import com.autoparts.mapper.attribute.AttributeValueCreateMapper;
import com.autoparts.repository.ProductAttributeRepository;
import com.autoparts.repository.ProductAttributeValueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProductAttributeService {

    private final ProductAttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;
    private final AttributeValueCreateMapper valueCreateMapper;
    private final ProductAttributeValueRepository attributeValueRepository;

    public List<ProductAttribute> findAll() {
        return attributeRepository.findAll().stream().toList();
    }

    public void create(List<AttributeCreateDto> createDto){

        Set<ProductAttribute> productAttributes = createDto.stream()
                .map(attributeCreateDto -> {
                    ProductAttribute productAttribute = attributeMapper.toEntity(attributeCreateDto);
                    Set<ProductAttributeValue> productAttributeValues = valueCreateMapper.toEntityList(attributeCreateDto.getValues());
                    productAttribute.setValues(productAttributeValues);

                    attributeValueRepository.saveAllAndFlush(productAttributeValues);

                    return productAttribute;
                }).collect(Collectors.toSet());

        attributeRepository.saveAllAndFlush(productAttributes);
        }

}
