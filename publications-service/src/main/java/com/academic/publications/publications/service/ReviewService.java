package com.academic.publications.publications.service;

import com.academic.publications.publications.dto.ReviewDTO;
import com.academic.publications.publications.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    Review createReview(ReviewDTO reviewDTO);
    Review getReviewById(Long id);
    Page<Review> getReviewsByPublicationId(Long publicationId, Pageable pageable);
    Review updateReview(Long id, ReviewDTO reviewDTO);
    void deleteReview(Long id);
}