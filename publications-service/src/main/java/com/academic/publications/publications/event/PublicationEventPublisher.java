package com.academic.publications.publications.event;

import com.academic.publications.publications.model.OutboxEvent;
import com.academic.publications.publications.model.Publication;
import com.academic.publications.publications.service.OutboxService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class PublicationEventPublisher {

    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PublicationEventPublisher(OutboxService outboxService, ObjectMapper objectMapper) {
        this.outboxService = outboxService;
        this.objectMapper = objectMapper;
    }

    public void publishPublicationSubmittedEvent(Publication publication) {
        PublicationEvent event = new PublicationEvent(
                publication.getId(),
                publication.getTitle(),
                publication.getStatus().name(),
                publication.getAuthors().stream()
                        .map(author -> author.getId())
                        .collect(Collectors.toList())
        );

        saveEventToOutbox("Publication", "PublicationSubmitted", event);
    }

    public void publishPublicationApprovedEvent(Publication publication) {
        PublicationEvent event = new PublicationEvent(
                publication.getId(),
                publication.getTitle(),
                publication.getStatus().name(),
                publication.getAuthors().stream()
                        .map(author -> author.getId())
                        .collect(Collectors.toList())
        );

        saveEventToOutbox("Publication", "PublicationApproved", event);
    }

    private void saveEventToOutbox(String aggregateType, String eventType, Object payload) {
        try {
            OutboxEvent outboxEvent = new OutboxEvent();
            outboxEvent.setAggregateType(aggregateType);
            outboxEvent.setAggregateId(payload instanceof PublicationEvent ?
                    ((PublicationEvent) payload).getPublicationId().toString() : "");
            outboxEvent.setEventType(eventType);
            outboxEvent.setPayload(objectMapper.writeValueAsString(payload));
            outboxEvent.setCreatedAt(LocalDateTime.now());
            outboxEvent.setProcessed(false);

            outboxService.saveEvent(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing event payload", e);
        }
    }
}