package com.autoparts.repository;

import com.autoparts.entity.AttributeGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeGroupRepository extends JpaRepository<AttributeGroup, Long> {

    @EntityGraph(attributePaths = {"attributes"})
    Page<AttributeGroup> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"attributes","attributes.values"})
    List<AttributeGroup> findAll();
}
