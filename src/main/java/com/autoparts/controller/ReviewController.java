package com.autoparts.controller;


import com.autoparts.dto.review.ReviewResponse;
import com.autoparts.entity.Review;
import com.autoparts.serivce.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("")
    public ReviewResponse<Review> findAll(@RequestParam Long id,Pageable pageable){
        Page<Review> page = reviewService.findById(id, pageable);
        return ReviewResponse.of(page);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Review review) {
        reviewService.create(review);
    }

}
