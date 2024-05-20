package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.entities.Review;
import com.example.untoldpsproject.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Controller class for managing review operations.
 */
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    /**
     * Retrieves all reviews.
     *
     * @return A list of Review objects representing all reviews.
     */
    @GetMapping("/list/{userId}")
    public ModelAndView getAllReviews(@PathVariable String userId) {
        ModelAndView modelAndView = new ModelAndView("reviewList");
        modelAndView.addObject("reviews", reviewService.getAllReviews());
        modelAndView.addObject("userId", userId);
        return  modelAndView;
    }
    @PostMapping
    public void addReview(@RequestBody Review review) {
        reviewService.addReview(review);
    }
}
