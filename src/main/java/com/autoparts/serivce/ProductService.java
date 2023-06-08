package com.autoparts.serivce;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.autoparts.bucket.BucketName;
import com.autoparts.dto.product.ProductDto;
import com.autoparts.dto.product.ProductExcelDto;
import com.autoparts.dto.product.ProductUpdateDto;
import com.autoparts.entity.Product;
import com.autoparts.mapper.product.ProductExcelMapper;
import com.autoparts.mapper.product.ProductMapper;
import com.autoparts.mapper.product.ProductUpdateMapper;
import com.autoparts.repository.CategoryRepository;
import com.autoparts.repository.ProductRepository;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.http.entity.ContentType.*;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ImageService imageService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ProductExcelMapper productExcelMapper;
    private final ProductUpdateMapper productUpdateMapper;

    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto).toList();
    }

    public Optional<ProductDto> findById(Long id) {
        return productRepository.findById(id).map(productMapper::toDto);
    }
    @Transactional
    public void create(ProductDto productDto) {
        Optional.of(productDto)
                .map(productDto1 -> {
                    uploadImage(productDto.getImage());
                    return productMapper.toEntity(productDto1);
                })
                .map(productRepository::save)
                .orElseThrow();
    }

    @Transactional
    public Optional<ProductDto> update(Long id, ProductUpdateDto productDto){
        return productRepository.findById(id)
                .map(entity->{
                    Product product = productUpdateMapper.toEntity(productDto);
                    return productRepository.saveAndFlush(product);
                })
                .map(productMapper::toDto);

    }

    @SneakyThrows
    @Transactional
    public void importExcel(MultipartFile file) {
        InputStream inputStream = file.getInputStream();

        List<ProductExcelDto> productDtoList = Poiji.fromExcel(inputStream, PoijiExcelType.XLSX, ProductExcelDto.class);

        Optional.of(productDtoList)
                .map(dto -> productExcelMapper.toEntityList(dto, categoryRepository))
                .map(productRepository::saveAll)
                .orElseThrow();
    }

    public void uploadImage(MultipartFile file) {
        isFileEmpty(file);
        isImage(file);
        ObjectMetadata metadata = metadata(file);

        String imagePath = String.format("%s", BucketName.PRODUCT_IMAGE.getBucketName());
        String filename = String.format("%s-%s", UUID.randomUUID(), file.getOriginalFilename());

        try {
            imageService.upload(imagePath, filename, metadata, file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Optional<byte[]> downloadProductImage(Long id) {
        String imagePath = String.format("%s", BucketName.PRODUCT_IMAGE.getBucketName());

        return productRepository.findById(id)
                .map(Product::getImage)
                .map(key -> imageService.get(imagePath, key));
    }



    @SneakyThrows
    private ObjectMetadata metadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        return metadata;
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType())
                .contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
    }

    public List<ProductDto> compareProducts(List<Long> ids){
        return productRepository.findByIdIn(ids).stream()
                .map(productMapper::toDto)
                .toList();
    }

    public void updateCountOfProducts(){
        Map<Long, Integer> map = productRepository.findAll().stream()
                .collect(Collectors.toMap(Product::getId, Product::getCount));
        System.out.println(map);
    }

}
