package com.autoparts.controller;

import com.autoparts.dto.brand.BrandCreateUpdateDto;
import com.autoparts.dto.brand.BrandDto;
import com.autoparts.entity.Brand;
import com.autoparts.serivce.BrandService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/api/v1/brand")
@AllArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public List<Brand> findAll(@RequestParam int size) {
        return brandService.findAll(size);
    }

    @GetMapping("/list")
    public List<BrandDto> findAllList() {
        return brandService.findAllList();
    }

    @GetMapping("/getAllNames")
    public List<String> getAllBrandNames() {
        return brandService.findAllBrandName();
    }

    @GetMapping("/popularNames")
    public List<String> popularNames() {
        return brandService.findPopularBrandNames();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody BrandCreateUpdateDto dto) {
        brandService.create(dto);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody BrandCreateUpdateDto dto, @PathVariable("id") Long id) {
        brandService.update(id ,dto);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            return brandService.delete(id)
                    ? noContent().build()
                    : notFound().build();
        }
        catch (Exception e){
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }
    }

    @PostMapping("/importExcel")
    @ResponseStatus(HttpStatus.CREATED)
    public void importExcel(@RequestParam("file") MultipartFile file) {
        brandService.importExcel(file);
    }
}
