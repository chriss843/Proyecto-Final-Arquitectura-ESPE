package com.academic.publications.publications.service;

import com.academic.publications.publications.model.OutboxEvent;
import com.academic.publications.publications.repository.OutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OutboxServiceImpl implements OutboxService {

    private final OutboxRepository outboxRepository;

    @Autowired
    public OutboxServiceImpl(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    @Override
    public void saveEvent(OutboxEvent event) {
        outboxRepository.save(event);
    }
}