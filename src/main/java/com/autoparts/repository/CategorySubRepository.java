package com.autoparts.repository;

import com.autoparts.entity.Category;
import com.autoparts.entity.CategorySub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorySubRepository extends JpaRepository<CategorySub, Long> {


}
