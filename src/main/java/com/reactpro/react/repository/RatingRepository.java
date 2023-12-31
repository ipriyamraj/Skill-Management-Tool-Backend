package com.reactpro.react.repository;

import com.reactpro.react.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByRating(int rating);
}
