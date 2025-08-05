package com.academic.publications.publications.event;

import com.academic.publications.publications.service.OutboxService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PublicationEventListener {

    private final OutboxService outboxService;

    @Autowired
    public PublicationEventListener(OutboxService outboxService) {
        this.outboxService = outboxService;
    }

    @RabbitListener(queues = "publication.events")
    public void handlePublicationEvent(PublicationEvent event) {
        // Aquí puedes procesar los eventos según su tipo
        System.out.println("Received publication event: " + event);
    }
}