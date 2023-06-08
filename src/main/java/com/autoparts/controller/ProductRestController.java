package com.autoparts.controller;

import com.autoparts.dto.product.ProductDto;
import com.autoparts.dto.product.ProductUpdateDto;
import com.autoparts.serivce.ProductService;
import com.autoparts.serivce.TovarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;
    private final TovarService tovarService;

    @GetMapping
    public List<ProductDto> findAll(){
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductDto findById(@PathVariable("id") Long id){
        return productService.findById(id)
                .orElseThrow();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@ModelAttribute ProductDto productDto) {
        productService.create(productDto);
    }

    @PutMapping("/{id}")
    public ProductDto update(@PathVariable("id") Long id,
                             @RequestBody ProductUpdateDto productDto) {
        return productService.update(id, productDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/{id}/image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] findProductImage(@PathVariable("id") Long id) {
        return productService.downloadProductImage(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/importExcel")
    @ResponseStatus(HttpStatus.CREATED)
    public void importExcel(@RequestParam("file") MultipartFile file) {
        productService.importExcel(file);
    }

    @GetMapping("/test")
    public void asd(){
        productService.updateCountOfProducts();
    }

    @GetMapping("/testasd")
    public void asasdd(){
        productService.updateCountOfProducts();
    }

    @GetMapping("/compare/{ids}")
    public List<ProductDto> findById(@PathVariable("ids") List<Long> ids){
        return productService.compareProducts(ids);
    }

}
