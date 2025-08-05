package com.academic.publications.publications.scheduler;

import com.academic.publications.publications.model.OutboxEvent;
import com.academic.publications.publications.repository.OutboxRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxScheduler {

    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public OutboxScheduler(OutboxRepository outboxRepository, RabbitTemplate rabbitTemplate) {
        this.outboxRepository = outboxRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void processOutboxEvents() {
        List<OutboxEvent> events = outboxRepository.findByProcessedFalse();

        for (OutboxEvent event : events) {
            try {
                rabbitTemplate.convertAndSend(
                        "publication.events",
                        event.getEventType(),
                        event.getPayload()
                );

                event.setProcessed(true);
                outboxRepository.save(event);
            } catch (Exception e) {
                System.err.println("Error processing outbox event " + event.getId() + ": " + e.getMessage());
            }
        }
    }
}