package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.entities.Review;
import com.example.untoldpsproject.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }
    @PostMapping
    public void addReview(@RequestBody Review review) {
        reviewService.addReview(review);
    }
}
