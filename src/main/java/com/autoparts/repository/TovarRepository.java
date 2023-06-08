package com.autoparts.repository;

import com.autoparts.entity.Brand;
import com.autoparts.entity.Tovar;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TovarRepository extends
        JpaRepository<Tovar, Long>,
        FilterProductRepository,
        QuerydslPredicateExecutor<Tovar> {


    @EntityGraph(attributePaths = {"brand",
            "category",
            "category.children",
            "images",
            "attributes",
            "type",
            "attributes.values",
            "type.attributeGroups",
            "type.attributeGroups.attributes"})
    List<Tovar> findAll();

    @EntityGraph(attributePaths = {"brand",
            "category",
            "category.children",
            "images",
            "attributes",
            "type",
            "attributes.values",
            "type.attributeGroups",
            "type.attributeGroups.attributes"})
    Optional<Tovar> findBySlug(String slug);@EntityGraph(attributePaths = {"brand",
            "category",
            "category.children",
            "images",
            "attributes",
            "type",
            "attributes.values",
            "type.attributeGroups",
            "type.attributeGroups.attributes"})

    Page<Tovar> findAll(Pageable pageable);@EntityGraph(attributePaths = {"brand",
            "category",
            "category.children",
            "images",
            "attributes",
            "type",
            "attributes.values",
            "type.attributeGroups",
            "type.attributeGroups.attributes"})

    Page<Tovar> findAllByCategoryName(String name, Pageable pageable);@EntityGraph(attributePaths = {"brand",
            "category",
            "category.children",
            "images",
            "attributes",
            "type",
            "attributes.values",
            "type.attributeGroups",
            "type.attributeGroups.attributes"})

    Page<Tovar> findAllByBrandName(String name, Pageable pageable);@EntityGraph(attributePaths = {"brand",
            "category",
            "category.children",
            "images",
            "attributes",
            "type",
            "attributes.values",
            "type.attributeGroups",
            "type.attributeGroups.attributes"})

    Page<Tovar> findAll(Predicate predicate, Pageable pageable);@EntityGraph(attributePaths = {"brand",
            "category",
            "category.children",
            "images",
            "attributes",
            "type",
            "attributes.values",
            "type.attributeGroups",
            "type.attributeGroups.attributes"})

    int countTovarByBrand(Brand brand);

    @Query("SELECT rating, COUNT(rating) as count FROM Tovar GROUP BY rating")
    List<Object[]> findReviewsByRating();

    Integer countTovarByCategoryName(String name);

    @EntityGraph(attributePaths = {"brand",
            "category",
            "category.children",
            "images",
            "attributes",
            "type",
            "attributes.values",
            "type.attributeGroups",
            "type.attributeGroups.attributes"})
    Page<Tovar> findTovarsByCategorySlug(Pageable pageable, String slug);

    @EntityGraph(attributePaths = {"brand",
            "category",
            "category.children",
            "images",
            "attributes",
            "type",
            "attributes.values",
            "type.attributeGroups",
            "type.attributeGroups.attributes"})
    List<Tovar> findByCompareAtPriceIsNotNullOrderByCompareAtPriceDescPriceDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"brand",
            "category",
            "category.children",
            "images",
            "attributes",
            "type",
            "attributes.values",
            "type.attributeGroups",
            "type.attributeGroups.attributes"})
    List<Tovar> findByIdIn(List<Long> ids);

    @Query("SELECT e.id as id, e.name as name FROM Tovar e")
    List<com.autoparts.dto.query.Tovar> findAllWithoutDepartment();
}
