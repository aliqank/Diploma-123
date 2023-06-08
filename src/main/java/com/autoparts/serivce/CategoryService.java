package com.autoparts.serivce;

import com.autoparts.dto.category.CategoryDto;
import com.autoparts.dto.category.CategoryUpdateDto;
import com.autoparts.dto.excel.CategoryExcelDto;
import com.autoparts.entity.Category;
import com.autoparts.entity.CategorySub;
import com.autoparts.mapper.category.CategoryMapper;
import com.autoparts.mapper.category.CategoryUpdateMapper;
import com.autoparts.repository.CategoryRepository;
import com.autoparts.repository.CategorySubRepository;
import com.autoparts.repository.TovarRepository;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategorySubRepository categorySubRepository;
    private final CategoryMapper categoryMapper;
    private TovarRepository tovarRepository;
    private CategoryUpdateMapper categoryUpdateMapper;

    private ExcelGeneratorService excelGeneratorService;

    public List<CategoryDto> findAllCategories(){
        return categoryRepository.findAll()
                .stream()
//                .peek(s->{
//                    Integer countOfProduct = getCountOfProduct(s.getName());
//                    s.setItems(countOfProduct);
//                })
                .map(categoryMapper::toDto)
                .toList();
    }

    public List<String> findAllCategoriesName(){
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .toList();
    }

    public Integer getCountOfProduct(String name){
        return tovarRepository.countTovarByCategoryName(name);
    }

    public List<CategorySub> findAllSubCategories(){
        return categorySubRepository.findAll();
    }

    public Optional<CategoryDto> findById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto);
    }

    public Optional<CategoryDto> findBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .map(categoryMapper::toDto);
    }

    public List<CategoryDto> findBySlugs(List<String> slug) {
        return categoryRepository.findBySlugIn(slug).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    public List<CategoryDto> findBySlugArray(List<String> slug) {
        return categoryRepository.findBySlugIn(slug).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Transactional
    public CategoryDto create(CategoryDto categoryDto){
        categoryDto.setType("shop");
        categoryDto.setLayout("products");
        categoryDto.setItems(0);
        return Optional.of(categoryDto)
                .map(categoryMapper::toEntity)
                .map(categoryRepository::save)
                .map(categoryMapper::toDto)
                .orElseThrow();
    }

    public List<CategoryDto> getPopularCategories(int size) {

        return categoryRepository.findAllOrderByPopularity(Pageable.ofSize(size)).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Transactional
    public CategoryDto update(Long id, CategoryUpdateDto categoryDto){

        Category category = categoryRepository.findById(id)
                .orElseThrow();
        categoryUpdateMapper.update(category, categoryDto);

        return Optional.of(category)
                .map(categoryRepository::save)
                .map(categoryMapper::toDto)
                .orElseThrow();
    }

    public List<String> findPopularCategoriesNames(){
       return tovarRepository.findPopularCategoryNames();
    }

    @Transactional
    public boolean delete(Long id) {
        return categoryRepository.findById(id)
                .map(entity -> {
                    categoryRepository.delete(entity);
                    categoryRepository.flush();
                    return true;
                })
                .orElse(false);
    }


    public ByteArrayInputStream excel(){
        List<Category> categories = categoryRepository.findAll();
        ByteArrayInputStream byteArrayInputStream = excelGeneratorService.listToExcelFile(categories);
        return byteArrayInputStream;
    }

    @SneakyThrows
    @Transactional
    public void importExcel(MultipartFile file) {
        InputStream inputStream = file.getInputStream();

        List<CategoryExcelDto> categoryExcelDtos = Poiji.fromExcel(inputStream, PoijiExcelType.XLSX, CategoryExcelDto.class);


        Optional.of(categoryExcelDtos)
                .map(dto -> {
                    List<Category> categories = categoryMapper.toEntityList(dto);

                    categories.forEach(s->{
                        s.setType("shop");
                        s.setLayout("products");
                        s.setItems(0);
                    });
                    return categories;
                })
                .map(categoryRepository::saveAll)
                .orElseThrow(() -> new RuntimeException("can't import files"));
    }
}
