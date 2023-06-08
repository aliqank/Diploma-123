package com.autoparts.serivce;

import com.autoparts.dto.attribute.AttributeGroupCreateDto;
import com.autoparts.entity.*;
import com.autoparts.mapper.attribute.AttributeGroupMapper;
import com.autoparts.mapper.attribute.AttributeMapper;
import com.autoparts.mapper.attribute.AttributeValueCreateMapper;
import com.autoparts.repository.AttributeGroupRepository;
import com.autoparts.repository.ProductAttributeValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttributeGroupService {

    private final AttributeGroupRepository attributeGroupRepository;
    private final AttributeGroupMapper attributeGroupMapper;
    private final AttributeMapper attributeMapper;

    private final AttributeValueCreateMapper valueCreateMapper;
    private final ProductAttributeValueRepository attributeValueRepository;

    public List<AttributeGroup> findAll() {
        return attributeGroupRepository.findAll().stream().toList();
    }

    public void create(Set<AttributeGroupCreateDto> createDto, ProductType productType, Tovar tovar) {
        Set<AttributeGroup> attributeGroups = createDto.stream()
                .map(attributeGroupCreateDto -> {
                    AttributeGroup attributeGroup = attributeGroupMapper.toEntity(attributeGroupCreateDto);
                    Set<ProductAttribute> productAttributes = attributeGroupCreateDto.getAttributes().stream()
                            .map(attributeCreateDto -> {
                                ProductAttribute productAttribute = attributeMapper.toEntity(attributeCreateDto);
                                productAttribute.setFeatured(true);
                                Set<ProductAttributeValue> productAttributeValues = valueCreateMapper.toEntityList(attributeCreateDto.getValues());

                                productAttribute.setValues(productAttributeValues);
//                                productAttribute.setAttributeGroup(attributeGroup);
                                productAttribute.setTovar(tovar);

                                attributeValueRepository.saveAllAndFlush(productAttributeValues);

                                return productAttribute;
                            }).collect(Collectors.toSet());

                    attributeGroup.setAttributes(productAttributes);
//                    attributeGroup.setProductType(productType);
                    return attributeGroup;
                }).collect(Collectors.toSet());
        productType.setAttributeGroups(attributeGroups);

        attributeGroupRepository.saveAllAndFlush(attributeGroups);
    }




}
