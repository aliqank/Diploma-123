package com.autoparts.controller;


import com.autoparts.dto.category.CategoryDto;
import com.autoparts.dto.filter.ProductFilter;
import com.autoparts.dto.filter.SearchFilter;
import com.autoparts.dto.response.PageResponse;
import com.autoparts.dto.response.SearchResponse;
import com.autoparts.dto.tovar.TovarCreateDto;
import com.autoparts.dto.tovar.TovarCreateEditDto;
import com.autoparts.dto.tovar.TovarDto;
import com.autoparts.dto.tovar.TovarUpdateDto;
import com.autoparts.repository.TovarRepository;
import com.autoparts.serivce.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/api/v1/tovars")
@RequiredArgsConstructor
public class TovarController {

    private final TovarService tovarService;
    private final ProductAttributeService productAttributeService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ReviewService reviewService;
    private final TovarRepository tovarRepository;
    private final AttributeGroupService attributeGroupService;
    private final ProductTypeService productTypeService;

    @PostMapping
    public void create(@RequestBody TovarCreateDto tovarCreateDto) {
        tovarService.create(tovarCreateDto);
    }

    @GetMapping()
    public PageResponse<TovarDto> findAll(ProductFilter filter, Pageable pageable){
        Page<TovarDto> page = tovarService.findAll(filter, pageable);
        return PageResponse.of(page,filter,categoryService, brandService,tovarService);
    }

    @GetMapping("/slug{slug}")
    public Optional<TovarDto> find(@PathVariable String slug){
       return tovarService.findProductBySlug(slug);
    }

    @GetMapping("/search")
    public SearchResponse search(SearchFilter filter, Pageable pageable){
        Page<TovarDto> products = tovarService.findByName(filter, pageable);
        Page<CategoryDto> categoryName = tovarService.findByCategoryName(filter, pageable);
        return SearchResponse.of(filter,products,categoryName);
    }

    @GetMapping("/featured")
    public List<TovarDto> featured(@RequestParam int size, String slug){
        return tovarService.getFeaturedProducts(slug, size);
    }

    @GetMapping("/stockValues")
    public List<String> stockValues(){
        return tovarService.getStockStatusValues();
    }

    @GetMapping("/featuredBrands")
    public List<TovarDto> featuredBrand(@RequestParam int size, String name){
        return tovarService.getFeaturedProductsByBrand(name, size);
    }

    @GetMapping("/topRated")
    public List<TovarDto> topRated(@RequestParam int size){
        return tovarService.getTopRatedProducts(size);
    }

    @GetMapping("/popular")
    public List<TovarDto> popular(@RequestParam int size){
        return tovarService.getPopularProducts(size);
    }

    @GetMapping("/mostSold")
    public List<TovarDto> mostSold(@RequestParam int size){
        return tovarService.findMostSoldProducts(size);
    }

    @GetMapping("/specialOffers")
    public List<TovarDto> SpecialOffers(@RequestParam int size){
        return tovarService.getSpecialOffers(size);
    }

    @PostMapping("/importExcel")
    @ResponseStatus(HttpStatus.CREATED)
    public void importExcel(@RequestParam("file") MultipartFile file) {
        tovarService.importExcel(file);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return tovarService.delete(id)
                ? noContent().build()
                : notFound().build();
    }

    //    @GetMapping("/asd")
//    public List<TovarDto> findAllDsl(ProductFilter filter){
//        List<TovarDto> page = tovarService.findAllDsl(filter);
//        return page;
//    }
    @PostMapping("/t1")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@ModelAttribute TovarCreateEditDto productDto) {
        tovarService.create(productDto, productDto.getImage());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void update(@RequestBody TovarUpdateDto tovarReadDto, @PathVariable("id") Long id) {
        tovarService.update(id ,tovarReadDto);
    }

}
