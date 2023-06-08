package com.autoparts.serivce;

import com.autoparts.dto.brand.BrandCreateUpdateDto;
import com.autoparts.dto.brand.BrandDto;
import com.autoparts.dto.excel.BrandExcelDto;
import com.autoparts.entity.Brand;
import com.autoparts.exception.ApiException;
import com.autoparts.mapper.brand.BrandMapper;
import com.autoparts.mapper.brand.BrandUpdateMapper;
import com.autoparts.repository.BrandRepository;
import com.autoparts.repository.TovarRepository;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandUpdateMapper updateMapper;
    private final BrandMapper brandMapper;
    private final TovarRepository tovarRepository;

    public List<Brand> findAll(int size){
        return brandRepository.findAll(Pageable.ofSize(size))
                .getContent().stream()
                .toList();
    }
    public List<Brand> findAll(){
        return brandRepository.findAll();
    }

    public List<BrandDto> findAllList(){
        return brandRepository.findAll().stream()
                .map(brandMapper::toDto)
                .toList();
    }

    public List<String> findPopularBrandNames(){
        return tovarRepository.findPopularBrandNames();
    }

    public List<String> findAllBrandName(){
        return brandRepository.findAll().stream()
                .map(Brand::getName)
                .toList();
    }

    @Transactional
    public boolean delete(Long id) {
        return brandRepository.findById(id)
                .map(entity -> {
                    brandRepository.delete(entity);
                    brandRepository.flush();
                    return true;
                })
                .orElseThrow(() -> new ApiException("Brand referenced to products", "402"));
    }

    @Transactional
    public BrandDto update(Long id, BrandCreateUpdateDto brandDto){

        Brand brand = brandRepository.findById(id)
                .orElseThrow();
        updateMapper.update(brand, brandDto);

        return Optional.of(brand)
                .map(brandRepository::save)
                .map(brandMapper::toDto)
                .orElseThrow();
    }

    @Transactional
    public BrandDto create(BrandCreateUpdateDto brandDto){
        return Optional.of(brandDto)
                .map(updateMapper::toEntity)
                .map(brandRepository::save)
                .map(brandMapper::toDto)
                .orElseThrow();
    }

    @SneakyThrows
    @Transactional
    public void importExcel(MultipartFile file) {
        InputStream inputStream = file.getInputStream();

        List<BrandExcelDto> brandExcelDtos = Poiji.fromExcel(inputStream, PoijiExcelType.XLSX, BrandExcelDto.class);


        Optional.of(brandExcelDtos)
                .map(dto -> {
                    List<Brand> brands = brandMapper.toList(dto);
                    return brands;
                })
                .map(brandRepository::saveAll)
                .orElseThrow(() -> new RuntimeException("can't import files"));
    }

}
