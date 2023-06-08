package com.autoparts.serivce;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.autoparts.bucket.BucketName;
import com.autoparts.dto.attribute.AttributeCreateDto;
import com.autoparts.dto.attribute.AttributeGroupCreateDto;
import com.autoparts.dto.category.CategoryDto;
import com.autoparts.dto.excel.TovarExcelDto;
import com.autoparts.dto.filter.ProductFilter;
import com.autoparts.dto.filter.SearchFilter;
import com.autoparts.dto.order.TovarReadDto;
import com.autoparts.dto.rating.RatingItemDto;
import com.autoparts.dto.tovar.TovarCreateDto;
import com.autoparts.dto.tovar.TovarCreateEditDto;
import com.autoparts.dto.tovar.TovarDto;
import com.autoparts.dto.tovar.TovarUpdateDto;
import com.autoparts.entity.*;
import com.autoparts.entity.enums.StockStatus;
import com.autoparts.mapper.attribute.AttributeValueCreateMapper;
import com.autoparts.mapper.category.CategoryMapper;
import com.autoparts.mapper.product.ProductImageMapper;
import com.autoparts.mapper.tovar.TovarCreateMapper;
import com.autoparts.mapper.tovar.TovarExcelMapper;
import com.autoparts.mapper.tovar.TovarMapper;
import com.autoparts.mapper.tovar.TovarReadMapper;
import com.autoparts.qerydsl.QPredicates;
import com.autoparts.repository.*;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.autoparts.entity.QCategory.category;
import static com.autoparts.entity.QTovar.tovar;
import static org.apache.http.entity.ContentType.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TovarService {

    private final TovarRepository tovarRepository;
    private final ImageService imageService;
    private final ProductImageRepository imageRepository;
    private final TovarMapper tovarMapper;
    private final TovarCreateMapper createMapper;
    private final ProductImageMapper imageMapper;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductTypeRepository typeRepository;
    private final AttributeGroupService  attributeGroupService;
    private final CategoryMapper categoryMapper;
    private final TovarExcelMapper excelMapper;
    private final UserService userService;
    private final ProductAttributeRepository attributeRepository;
    private final AttributeValueCreateMapper valueCreateMapper;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final TovarReadMapper tovarReadMapper;

    public Page<TovarDto> findAll(ProductFilter filter, Pageable pageable) {

        var predicates = QPredicates.builder()
                .add(filter.min() != null ? filter.min() : Integer.MIN_VALUE, tovar.price::goe)
                .add(filter.max() != null ? filter.max() : Integer.MAX_VALUE, tovar.price::loe)
                .add(filter.category(), tovar.category.slug::containsIgnoreCase)
                .add(filter.brand(), tovar.brand.slug::containsIgnoreCase)
                .add(filter.rating(), tovar.rating::in)
                .add(filter.car(), tovar.name::containsIgnoreCase)
                .build();

        return tovarRepository.findAll(predicates, pageable)
                .map(tovarMapper::toDto);
    }

    public List<TovarDto> findAllDsl(ProductFilter filter) {
        var predicates = QPredicates.builder()
                .add(filter.min() != null ? filter.min() : Integer.MIN_VALUE, tovar.price::goe)
                .add(filter.max() != null ? filter.max() : Integer.MAX_VALUE, tovar.price::loe)
                .add(filter.category(), tovar.category.slug::containsIgnoreCase)
                .add(filter.brand(), tovar.brand.slug::containsIgnoreCase)
                .add(filter.rating(), tovar.rating::in)
                .build();

        return tovarRepository.findAllByFilter(filter).stream()
                .map(tovarMapper::toDto)
                .toList();
    }


    public Page<TovarDto> findByName(SearchFilter filter, Pageable pageable) {

        var predicates = QPredicates.builder()
                .add(filter.name(), tovar.name::containsIgnoreCase)
                .build();

        return tovarRepository.findAll(predicates, pageable)
                .map(tovarMapper::toDto);

    }

    public Page<CategoryDto> findByCategoryName(SearchFilter filter, Pageable pageable) {

        var predicates = QPredicates.builder()
                .add(filter.name(), category.name::containsIgnoreCase)
                .build();

        return categoryRepository.findAll(predicates, pageable)
                .map(categoryMapper::toDto);

    }

    public List<Tovar> findAll() {
        return tovarRepository.findAll();
    }

    public Optional<TovarDto> findProductBySlug(String slug) {
        return tovarRepository.findBySlug(slug)
                .map(tovarMapper::toDto);

    }

    public List<RatingItemDto> getRatingAndCount() {
        List<Object[]> reviewsByRating = tovarRepository.findReviewsByRating();

        return reviewsByRating.stream()
                .map(result -> new RatingItemDto((int) result[0], (Long) result[1]))
                .sorted(Comparator.comparing(RatingItemDto::getRating).reversed())
                .collect(Collectors.toList());
    }

    @Transactional
    public Tovar create(TovarCreateDto tovarDTO) {

        ProductType productType = ProductType.builder()
                .name("Default")
                .slug("default")
                .build();

        List<AttributeCreateDto> attributeCreateDtos = tovarDTO.getAttributeGroup().stream().map(AttributeGroupCreateDto::getAttributes).flatMap(Collection::stream).toList();


        Tovar tovar = createMapper.toEntity(tovarDTO);

        Brand brand = brandRepository.findById(tovarDTO.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        Category category = categoryRepository.findById(tovarDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<ProductImage> images = tovarDTO.getImages()
                .stream()
                .map(imageMapper::toEntity)
                .toList();


        tovar.setRating(0);
        tovar.setReviews(0);
        tovar.setImages(images);
        tovar.setBrand(brand);
        tovar.setCategory(category);
        tovar.setType(productType);

        typeRepository.save(productType);
        Tovar savedTovar = tovarRepository.save(tovar);
        imageRepository.saveAllAndFlush(images);
        attributeGroupService.create(tovarDTO.getAttributeGroup(), productType, tovar);

        return savedTovar;
    }

    @Transactional
    public Tovar createByName(TovarCreateEditDto tovarDTO) {

        ProductType productType = ProductType.builder()
                .name("Default")
                .slug("default")
                .build();

        List<AttributeCreateDto> attributeCreateDtos = tovarDTO.getAttributeGroup().stream().map(AttributeGroupCreateDto::getAttributes).flatMap(Collection::stream).toList();


        Tovar tovar = createMapper.toEntity(tovarDTO);

        Brand brand = brandRepository.findByName(tovarDTO.getBrand())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        Category category = categoryRepository.findByName(tovarDTO.getCategory());

        List<ProductImage> images = tovarDTO.getImages()
                .stream()
                .map(imageMapper::toEntity)
                .toList();


        tovar.setRating(0);
        tovar.setReviews(0);
        tovar.setImages(images);
        tovar.setBrand(brand);
        tovar.setCategory(category);
        tovar.setType(productType);

        typeRepository.save(productType);
        Tovar savedTovar = tovarRepository.save(tovar);
        imageRepository.saveAllAndFlush(images);
        attributeGroupService.create(tovarDTO.getAttributeGroup(), productType, tovar);

        return savedTovar;
    }

    @Transactional
    public void create(TovarCreateEditDto tovarCreateDto, MultipartFile file) {
        Optional.of(tovarCreateDto)
                .map(productDto1 -> {
                    String filename = String.format("%s-%s", UUID.randomUUID(), file.getName());
                    uploadImage(file, filename);
                    List<ProductImage> images = new ArrayList<>();
                    images.add(ProductImage.builder()
                            .name("https://autoparts-product-image.s3.eu-north-1.amazonaws.com/" + filename)
                            .build());
                    var entity = createMapper.toEntity(productDto1);
                    entity.setImages(images);
                    return entity;
                })
                .map(tovarRepository::save)
                .orElseThrow();
    }


    public void updateProductRating(Long productId, int rating, int reviews) {
        Tovar tovar = tovarRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
        tovar.setRating(rating);
        tovar.setReviews(reviews);
        tovarRepository.save(tovar);

    }

    public List<TovarDto> getProducts() {

        return tovarRepository.findAll(Pageable.ofSize(8))
                .getContent().stream()
                .map(tovarMapper::toDto)
                .toList();
    }

    public List<TovarDto> getPopularProducts(int size) {

        return tovarRepository.findPopularProducts(size).stream()
                .map(tovarMapper::toDto)
                .toList();
    }

    public List<TovarDto> findMostSoldProducts(int size) {

        return tovarRepository.findMostSoldProducts(size).stream()
                .map(tovarMapper::toDto)
                .toList();
    }

    public List<TovarDto> getFeaturedProducts(String name, int size) {

//        PageRequest pageRequest = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "price"));

        return tovarRepository.findPopularProductsByCategory(name, size).stream()
                .map(tovarMapper::toDto)
                .toList();
    }

    public List<TovarDto> getFeaturedProductsByBrand(String name, int size) {

//        PageRequest pageRequest = PageRequest.of(0, size, Sort.by(Sort.Direction.ASC, "price"));

        return tovarRepository.findPopularProductsByBrand(name, size).stream()
                .map(tovarMapper::toDto)
                .toList();
    }

    public List<ProductAttribute> geAttr() {

        return attributeRepository.findAll();
    }

    public List<TovarDto> getTopRatedProducts(int size) {

        PageRequest pageRequest = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "rating"));

        return tovarRepository.findAll(pageRequest)
                .getContent().stream()
                .map(tovarMapper::toDto)
                .toList();
    }

    public List<TovarDto> getSpecialOffers(int size) {
        return tovarRepository.findSpecialProducts(Pageable.ofSize(size))
                .stream()
                .map(tovarMapper::toDto)
                .toList();
    }

    public List<TovarDto> getTovars() {
        return tovarRepository.findAll()
                .stream()
                .map(tovarMapper::toDto)
                .toList();
    }

    @Transactional
    public boolean delete(Long id) {
        return tovarRepository.findById(id)
                .map(entity -> {
                    tovarRepository.delete(entity);
                    tovarRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public TovarReadDto update(Long id, TovarUpdateDto tovarReadDto) {

        Tovar tovar = tovarRepository.findById(id).orElseThrow();
        tovarReadMapper.update(tovar, tovarReadDto);

        return Optional.of(tovar)
                .map(tovarRepository::save)
                .map(tovarReadMapper::toDto)
                .orElseThrow();
    }

    public List<String> getStockStatusValues() {
        return Arrays.stream(StockStatus.values())
                .map(Enum::name)
                .toList();
    }

    private void trimStringFields(TovarExcelDto dto) {
        // get all fields of the dto class
        Field[] fields = TovarExcelDto.class.getDeclaredFields();

        for (Field field : fields) {
            // if the field is a String, trim its value
            if (field.getType().equals(String.class)) {
                try {
                    field.setAccessible(true);
                    String value = (String) field.get(dto);
                    if (value != null) {
                        field.set(dto, value.trim());
                    }
                } catch (IllegalAccessException e) {
                    // handle exception
                }
            }
        }
    }


    @SneakyThrows
    @Transactional
    public void importExcel(MultipartFile file) {
        InputStream inputStream = file.getInputStream();

        List<TovarExcelDto> tovarDtoList = Poiji.fromExcel(inputStream, PoijiExcelType.XLSX, TovarExcelDto.class);

        for(TovarExcelDto dto : tovarDtoList) {
            trimStringFields(dto);
        }

        if(tovarDtoList.isEmpty()) {
            throw new RuntimeException("Tovar List is empty");
        }

        List<Tovar> tovars = new ArrayList<>();

        for(TovarExcelDto dto : tovarDtoList){
            List<String> attributesValues = Collections.singletonList(dto.getAttributesValue());
            Tovar s = excelMapper.toEntity(dto, categoryRepository, brandRepository);
            s.setReviews(0);
            s.setRating(0);
            s.setStock(StockStatus.IN_STOCK);
            s.setType(
                    ProductType.builder()
                            .name("Type")
                            .slug("Type")
                            .attributeGroups(new HashSet<>(parseObject1(attributesValues, s)))
                            .build()
            );
            tovars.add(s);
        }

        tovarRepository.saveAll(tovars);
    }




    public static List<AttributeGroup> parseObject1(List<String> inputList, Tovar tovar) {
        Map<String, AttributeGroup> attributeGroupMap = new HashMap<>();
        for(String str : inputList) {
            String[] arr = str.split(",");
            for(String s : arr) {
                String[] parts = s.split("-");
                if(!attributeGroupMap.containsKey(parts[0])) {
                    // Create a new group if it does not exist
                    AttributeGroup attributeGroup = AttributeGroup.builder()
                            .name(parts[0])
                            .slug(parts[0])
                            .attributes(new HashSet<>()) // initialize with an empty set
                            .build();
                    attributeGroupMap.put(parts[0], attributeGroup);
                }
                // Update the group's attributes
                AttributeGroup existingGroup = attributeGroupMap.get(parts[0]);
                Set<ProductAttribute> updatedAttributes = new HashSet<>(parseObject2(parts[1], tovar));
                existingGroup.getAttributes().addAll(updatedAttributes);
            }
        }
        return new ArrayList<>(attributeGroupMap.values());
    }



    public static List<ProductAttribute> parseObject2(String str, Tovar tovar) {
        List<ProductAttribute> productAttributes = new ArrayList<>();
        String[] arr = str.split(",");
        for(String s : arr) {
            String[] parts = s.contains(":") ? s.split(":") : new String[]{s};
            List<ProductAttributeValue> object3List = parts.length > 1 ? parseObject3(parts[1]) : new ArrayList<>();
            Set<ProductAttributeValue> valueSet = new HashSet<>(object3List);
            ProductAttribute productAttribute = ProductAttribute.builder()
                    .name(parts[0])
                    .slug(parts[0])
                    .values(valueSet)
                    .tovar(tovar)
                    .build();
            productAttributes.add(productAttribute);
        }
        return productAttributes;
    }

    public static List<ProductAttributeValue> parseObject3(String str) {
        List<ProductAttributeValue> productAttributeValues = new ArrayList<>();
        String[] arr = str.split(",");
        for(String s : arr) {
            String[] parts = s.contains(":") ? s.split(":") : new String[]{s};
            ProductAttributeValue productAttributeValue = ProductAttributeValue.builder()
                    .name(parts[0])
                    .slug(parts[0])
                    .build();
            productAttributeValues.add(productAttributeValue);
        }
        return productAttributeValues;
    }


    //    public static List<ProductAttribute> parseObject2(String str) {
//        return Arrays.stream(str.split(","))
//                .map(s -> {
//                    String[] parts = s.split(":");
//                    return ProductAttribute.builder()
//                            .name(parts[0])
//                            .slug(parts[0])
//                            .values(parseObject3(parts[1]))
//                            .build();
//                })
//                .collect(Collectors.toList());
//    }


    public List<TovarReadDto> findAllDslasd() {
       return tovarRepository.findAllDsl(10).stream()
               .map(tovarReadMapper::toDto)
               .toList();
    }

    public void uploadImage(MultipartFile file, String filename) {
        isFileEmpty(file);
        isImage(file);
        ObjectMetadata metadata = metadata(file);

        String imagePath = String.format("%s", BucketName.PRODUCT_IMAGE.getBucketName());

        try {
            imageService.upload(imagePath, filename, metadata, file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
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

}
