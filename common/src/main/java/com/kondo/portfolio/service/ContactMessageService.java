package com.kondo.portfolio.service;

import com.kondo.portfolio.domain.ContactMessage;
import com.kondo.portfolio.repository.ContactMessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactMessageService {

    private final ContactMessageRepository repository;

    public ContactMessageService(ContactMessageRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ContactMessage receive(String name, String email, String message) {
        ContactMessage m = new ContactMessage();
        m.setName(name);
        m.setEmail(email);
        m.setMessage(message);
        m.setCreatedAt(LocalDateTime.now());
        return repository.save(m);
    }

    @Transactional(readOnly = true)
    public List<ContactMessage> findAllRecentFirst() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Optional<ContactMessage> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public void markAsRead(Long id) {
        repository.findById(id).ifPresent(m -> m.setReadAt(LocalDateTime.now()));
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
