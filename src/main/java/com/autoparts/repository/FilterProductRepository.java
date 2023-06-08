package com.autoparts.repository;

import com.autoparts.dto.filter.ProductFilter;
import com.autoparts.dto.filter.SearchFilter;
import com.autoparts.entity.Category;
import com.autoparts.entity.Tovar;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FilterProductRepository {
    List<Tovar> findAllByFilter(ProductFilter filter);
    List<Tovar> searchByName(SearchFilter filter);
    List<Category> searchByCategoryName(SearchFilter filter);
    List<Tovar> findAllDsl(int size);

    List<String> findPopularCategoryNames();

    List<String> findPopularBrandNames();
    List<Tovar> findSpecialProducts(Pageable pageable);
    List<String> findProductNamesByIdIN(List<Long> ids);
    List<Tovar> findPopularProductsByBrand(String brandName, int limit);
    List<Tovar> findPopularProductsByCategory(String name, int limit);
    List<Tovar> findPopularProducts(int limit);

    List<Tovar> findMostSoldProducts(int limit);
}
