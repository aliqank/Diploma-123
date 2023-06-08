package com.autoparts.controller;

import com.autoparts.dto.category.CategoryDto;
import com.autoparts.dto.category.CategoryUpdateDto;
import com.autoparts.entity.CategorySub;
import com.autoparts.serivce.CategoryService;
import com.autoparts.serivce.TovarService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/api/v1/category")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final TovarService tovarService;

    @GetMapping()
    public List<CategoryDto> findAll() {
        return categoryService.findAllCategories();
    }

    @GetMapping("/subcategory")
    public List<CategorySub> findAllSub() {
        return categoryService.findAllSubCategories();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CategoryDto categoryDto) {
        categoryService.create(categoryDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void update(@RequestBody CategoryUpdateDto categoryDto, @PathVariable("id") Long id) {
        categoryService.update(id ,categoryDto);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return categoryService.delete(id)
                ? noContent().build()
                : notFound().build();
    }

    @GetMapping("/{id}")
    public CategoryDto findById(@PathVariable("id") Long id) {
        return categoryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getAllNames")
    public List<String> getAllCategoryNames() {
        return categoryService.findAllCategoriesName();
    }

    @GetMapping("/popularNames")
    public List<String> popularNames() {
        return categoryService.findPopularCategoriesNames();
    }

    @GetMapping("/findBySlug")
    public Optional<CategoryDto> findBySlug(@RequestParam("slug") String slug) {
        return categoryService.findBySlug(slug);
    }

    @GetMapping("/findBProductBySlugs")
    public List<CategoryDto> findProductBySLugs(@RequestParam("slug") String[] slug) {
        List<String> strings = Arrays.asList(slug);
        return categoryService.findBySlugArray(strings);
    }

    @GetMapping("/categories/slugs")
    public ResponseEntity<List<CategoryDto>> getCategoriesBySlugs(@RequestParam("slugs") List<String> slugs) {
        List<CategoryDto> categories = categoryService.findBySlugs(slugs);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/popularCategories")
    public List<CategoryDto> popularCategories(@RequestParam int size) {
        return categoryService.getPopularCategories(size);
    }

    @PostMapping("/importExcel")
    @ResponseStatus(HttpStatus.CREATED)
    public void importExcel(@RequestParam("file") MultipartFile file) {
        categoryService.importExcel(file);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile() {
        ByteArrayInputStream excel = categoryService.excel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=objects.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ByteArrayResource(excel.readAllBytes()));
    }

}
