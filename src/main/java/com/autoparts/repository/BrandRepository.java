package com.autoparts.repository;

import com.autoparts.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    Page<Brand> findAll(Pageable pageable);
    Optional<Brand> findByName(String name);

}
