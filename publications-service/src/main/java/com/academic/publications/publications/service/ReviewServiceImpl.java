package com.academic.publications.publications.service;

import com.academic.publications.publications.dto.ReviewDTO;
import com.academic.publications.publications.model.Publication;
import com.academic.publications.publications.model.Review;
import com.academic.publications.publications.repository.PublicationRepository;
import com.academic.publications.publications.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PublicationRepository publicationRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             PublicationRepository publicationRepository) {
        this.reviewRepository = reviewRepository;
        this.publicationRepository = publicationRepository;
    }

    @Override
    public Review createReview(ReviewDTO reviewDTO) {
        Publication publication = publicationRepository.findById(reviewDTO.getPublicationId())
                .orElseThrow(() -> new RuntimeException("Publication not found"));

        Review review = new Review();
        review.setPublication(publication);
        review.setReviewerId(reviewDTO.getReviewerId());
        review.setComments(reviewDTO.getComments());
        review.setRating(reviewDTO.getRating());
        review.setReviewDate(new Date());
        review.setApproved(reviewDTO.isApproved());

        return reviewRepository.save(review);
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    @Override
    public Page<Review> getReviewsByPublicationId(Long publicationId, Pageable pageable) {
        return reviewRepository.findByPublicationId(publicationId, pageable);
    }

    @Override
    public Review updateReview(Long id, ReviewDTO reviewDTO) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        existingReview.setComments(reviewDTO.getComments());
        existingReview.setRating(reviewDTO.getRating());
        existingReview.setApproved(reviewDTO.isApproved());

        return reviewRepository.save(existingReview);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}