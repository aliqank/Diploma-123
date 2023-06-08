package com.autoparts.repository;

import com.autoparts.entity.ProductAttribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
//    List<ProductAttribute> findProductAttributesByAttributeGroupName(String name);

    @EntityGraph(attributePaths = {"values"})
    Page<ProductAttribute> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"values"})
    List<ProductAttribute> findAll();

    @EntityGraph(attributePaths = {"values"})
    List<ProductAttribute> findAllByIdIn(Set<Long> ids);




}
