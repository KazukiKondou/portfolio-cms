package com.kondo.portfolio.service;

import com.kondo.portfolio.domain.TimelineEvent;
import com.kondo.portfolio.repository.TimelineEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TimelineEventService {

    private final TimelineEventRepository repository;

    public TimelineEventService(TimelineEventRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TimelineEvent> findPublished() {
        return repository.findPublishedInOrder();
    }

    @Transactional(readOnly = true)
    public List<TimelineEvent> findAllOrdered() {
        return repository.findAllInOrder();
    }

    @Transactional(readOnly = true)
    public Optional<TimelineEvent> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public TimelineEvent save(TimelineEvent event) {
        LocalDateTime now = LocalDateTime.now();
        if (event.getId() == null) {
            event.setCreatedAt(now);
        }
        event.setUpdatedAt(now);
        if (event.getPublished() == null) event.setPublished(true);
        if (event.getSortOrder() == null) event.setSortOrder(0);
        return repository.save(event);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
