package com.autoparts.serivce;

import com.autoparts.entity.ProductImage;
import com.autoparts.repository.ProductImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProductImageService {

    private final ProductImageRepository imageRepository;

    public void createAll(List<ProductImage> image){
        imageRepository.saveAll(image);
    }
}
