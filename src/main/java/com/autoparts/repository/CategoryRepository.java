package com.autoparts.repository;

import com.autoparts.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends
        JpaRepository<Category, Long>,
        FilterProductRepository,
        QuerydslPredicateExecutor<Category> {
    @EntityGraph(attributePaths = {"children"})
    List<Category> findAll();
    @EntityGraph(attributePaths = {"children"})
    Category findByName(String name);

    @EntityGraph(attributePaths = {"children"})
    Optional<Category> findBySlug(String slug);

    @EntityGraph(attributePaths = {"children"})
    List<Category> findBySlugIn(List<String> slugs);

//    @EntityGraph(attributePaths = {"subCategory"})
//    @Query("SELECT c FROM Category c LEFT JOIN fetch c.children sc " +
//            "GROUP BY c.id " +
//            "ORDER BY (SUM(COALESCE(sc.id, 0)) + c.items) DESC")

    @EntityGraph(attributePaths = {"children"})
    @Query("SELECT c FROM Category c LEFT JOIN fetch c.children sc " +
            "GROUP BY c.id, sc.id " +
            "ORDER BY (SUM(COALESCE(sc.id, 0)) + c.items) DESC")
    List<Category> findAllOrderByPopularity(Pageable pageable);
}
