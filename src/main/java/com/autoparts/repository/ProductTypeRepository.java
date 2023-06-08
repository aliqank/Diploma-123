package com.autoparts.repository;

import com.autoparts.entity.ProductType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

    @EntityGraph("productType-entity-graph")
    ProductType findBySlug(String slug);
}
