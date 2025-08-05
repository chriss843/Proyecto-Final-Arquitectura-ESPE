package com.academic.publications.publications.service;

import com.academic.publications.publications.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    Author createAuthor(Author author);
    Author getAuthorById(Long id);
    Page<Author> getAllAuthors(Pageable pageable);
    Author updateAuthor(Long id, Author author);
    void deleteAuthor(Long id);
}