package com.academic.publications.publications.service;

import com.academic.publications.publications.dto.ArticleDTO;
import com.academic.publications.publications.dto.BookDTO;
import com.academic.publications.publications.dto.PublicationDTO;
import com.academic.publications.publications.model.*;
import com.academic.publications.publications.repository.AuthorRepository;
import com.academic.publications.publications.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository publicationRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PublicationServiceImpl(PublicationRepository publicationRepository,
                                  AuthorRepository authorRepository) {
        this.publicationRepository = publicationRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public Publication createPublication(PublicationDTO publicationDTO) {
        Publication publication;

        if (publicationDTO instanceof ArticleDTO) {
            Article article = new Article();
            mapArticleDTO((ArticleDTO) publicationDTO, article);
            publication = article;
        } else if (publicationDTO instanceof BookDTO) {
            Book book = new Book();
            mapBookDTO((BookDTO) publicationDTO, book);
            publication = book;
        } else {
            throw new IllegalArgumentException("Unsupported publication type");
        }

        mapCommonPublicationFields(publicationDTO, publication);
        publication.setStatus(PublicationStatus.DRAFT);

        List<Author> authors = authorRepository.findAllById(publicationDTO.getAuthorIds());
        publication.setAuthors(authors);

        return publicationRepository.save(publication);
    }

    @Override
    @Transactional
    public Publication updatePublication(Long id, PublicationDTO publicationDTO) {
        Publication existingPublication = publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publication not found"));

        mapCommonPublicationFields(publicationDTO, existingPublication);

        if (existingPublication instanceof Article && publicationDTO instanceof ArticleDTO) {
            mapArticleDTO((ArticleDTO) publicationDTO, (Article) existingPublication);
        } else if (existingPublication instanceof Book && publicationDTO instanceof BookDTO) {
            mapBookDTO((BookDTO) publicationDTO, (Book) existingPublication);
        }

        List<Author> authors = authorRepository.findAllById(publicationDTO.getAuthorIds());
        existingPublication.setAuthors(authors);

        return publicationRepository.save(existingPublication);
    }

    @Override
    public Publication getPublicationById(Long id) {
        return publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publication not found"));
    }

    @Override
    public Page<Publication> getAllPublications(Pageable pageable) {
        return publicationRepository.findAll(pageable);
    }

    @Override
    public void deletePublication(Long id) {
        publicationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Publication changeStatus(Long id, PublicationStatus status) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publication not found"));

        publication.setStatus(status);
        return publicationRepository.save(publication);
    }

    private void mapCommonPublicationFields(PublicationDTO dto, Publication publication) {
        publication.setTitle(dto.getTitle());
        publication.setSummary(dto.getSummary());
    }

    private void mapArticleDTO(ArticleDTO dto, Article article) {
        article.setDoi(dto.getDoi());
        article.setJournal(dto.getJournal());
        article.setVolume(dto.getVolume());
        article.setIssue(dto.getIssue());
        article.setPageFrom(dto.getPageFrom());
        article.setPageTo(dto.getPageTo());
    }

    private void mapBookDTO(BookDTO dto, Book book) {
        book.setIsbn(dto.getIsbn());
        book.setPublisher(dto.getPublisher());
        book.setEdition(dto.getEdition());
        book.setTotalPages(dto.getTotalPages());
    }
}