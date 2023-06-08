package com.autoparts.repository;

import com.autoparts.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAll(Pageable pageable);
    Page<Review> findByProductId(Long productId,Pageable pageable);
    List<Review> findByProductId(Long productId);

    @Query("SELECT rating, COUNT(rating) as count FROM Review GROUP BY rating")
    List<Object[]> findReviewsByRating();

}
