package com.academic.publications.publications.repository;

import com.academic.publications.publications.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPublicationId(Long publicationId); // Método existente

    // Añade este nuevo método para soportar paginación
    Page<Review> findByPublicationId(Long publicationId, Pageable pageable);
}