package com.academic.publications.publications.service;

import com.academic.publications.publications.dto.PublicationDTO;
import com.academic.publications.publications.model.Publication;
import com.academic.publications.publications.model.PublicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublicationService {
    Publication createPublication(PublicationDTO publicationDTO);
    Publication updatePublication(Long id, PublicationDTO publicationDTO);
    Publication getPublicationById(Long id);
    Page<Publication> getAllPublications(Pageable pageable);
    void deletePublication(Long id);
    Publication changeStatus(Long id, PublicationStatus status);
}