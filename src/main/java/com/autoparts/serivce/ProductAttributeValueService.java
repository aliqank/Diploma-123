package com.autoparts.serivce;

import com.autoparts.repository.ProductAttributeValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductAttributeValueService {

    private final ProductAttributeValueRepository attributeValueRepository;

}
