package com.example.untoldpsproject.services;

import com.example.untoldpsproject.entities.Review;
import com.example.untoldpsproject.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Service class for managing reviews.
 */
@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * Retrieves all reviews from the database.
     *
     * @return A list of reviews.
     */
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
    /**
     * Adds a new review to the database.
     *
     * @param review The review to be added.
     */
    public void addReview(Review review) {
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }
}
