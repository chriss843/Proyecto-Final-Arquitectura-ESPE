package com.academic.publications.publications.event;

import java.util.List;

public class PublicationEvent {

    private Long publicationId;
    private String title;
    private String status;
    private List<Long> authorIds;

    public PublicationEvent() {
    }

    public PublicationEvent(Long publicationId, String title, String status, List<Long> authorIds) {
        this.publicationId = publicationId;
        this.title = title;
        this.status = status;
        this.authorIds = authorIds;
    }

    // Getters and Setters
    public Long getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(Long publicationId) {
        this.publicationId = publicationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<Long> authorIds) {
        this.authorIds = authorIds;
    }
}