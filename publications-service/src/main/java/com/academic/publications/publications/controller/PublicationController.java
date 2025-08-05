package com.academic.publications.publications.controller;

import com.academic.publications.publications.dto.PublicationDTO;
import com.academic.publications.publications.model.Publication;
import com.academic.publications.publications.model.PublicationStatus;
import com.academic.publications.publications.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publications")
public class PublicationController {

    private final PublicationService publicationService;

    @Autowired
    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @PostMapping
    public ResponseEntity<Publication> createPublication(@RequestBody PublicationDTO publicationDTO) {
        Publication publication = publicationService.createPublication(publicationDTO);
        return ResponseEntity.ok(publication);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publication> getPublication(@PathVariable Long id) {
        Publication publication = publicationService.getPublicationById(id);
        return ResponseEntity.ok(publication);
    }

    @GetMapping
    public ResponseEntity<Page<Publication>> getAllPublications(Pageable pageable) {
        Page<Publication> publications = publicationService.getAllPublications(pageable);
        return ResponseEntity.ok(publications);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publication> updatePublication(
            @PathVariable Long id,
            @RequestBody PublicationDTO publicationDTO) {
        Publication publication = publicationService.updatePublication(id, publicationDTO);
        return ResponseEntity.ok(publication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        publicationService.deletePublication(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Publication> changeStatus(
            @PathVariable Long id,
            @RequestParam PublicationStatus status) {
        Publication publication = publicationService.changeStatus(id, status);
        return ResponseEntity.ok(publication);
    }
}