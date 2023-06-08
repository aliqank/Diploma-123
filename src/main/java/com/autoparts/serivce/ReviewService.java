package com.autoparts.serivce;

import com.autoparts.entity.Review;
import com.autoparts.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TovarService tovarService;
    private final UserService userService;

    public Page<Review> findAll(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    public Page<Review> findById(Long productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable);
    }

    public List<Review> findById(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Transactional
    public void create(Review review) {
        String currentUser = userService.getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));
        review.setAuthor(currentUser);
        review.setDate(LocalDate.now());
        review.setAvatar("https://images.unsplash.com/photo-1633332755192-727a05c4013d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8dXNlcnxlbnwwfHwwfHw%3D&w=1000&q=80");
        reviewRepository.save(review);
        updateProductRating(review.getProductId());
    }

    private void updateProductRating(Long productId) {
        int rating = getProductRating(productId);
        int reviews = getProductReviews(productId);
        tovarService.updateProductRating(productId, rating,reviews);
    }

    private int getProductRating(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        if (reviews.isEmpty()) {
            return 0;
        }
        int sum = reviews.stream().mapToInt(Review::getRating).sum();
        return sum / reviews.size();
    }

    private int getProductReviews(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        return reviews.size();
    }
}
