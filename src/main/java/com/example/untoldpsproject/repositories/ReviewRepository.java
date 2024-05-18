package com.example.untoldpsproject.repositories;

import com.example.untoldpsproject.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
