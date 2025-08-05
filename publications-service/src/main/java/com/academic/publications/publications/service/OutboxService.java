package com.academic.publications.publications.service;

import com.academic.publications.publications.model.OutboxEvent;

public interface OutboxService {
    void saveEvent(OutboxEvent event);
}