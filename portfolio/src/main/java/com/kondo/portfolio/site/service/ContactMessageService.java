package com.kondo.portfolio.site.service;

import com.kondo.portfolio.entity.ContactMessage;
import com.kondo.portfolio.repository.ContactMessageRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 公開サイト用のお問い合わせ受信サービス */
@Service
public class ContactMessageService {

    private final ContactMessageRepository repository;

    public ContactMessageService(ContactMessageRepository repository) {
        this.repository = repository;
    }

    /** フォームから受信したメッセージを保存する */
    @Transactional
    public ContactMessage receive(String name, String email, String message) {
        ContactMessage m = new ContactMessage();
        m.setName(name);
        m.setEmail(email);
        m.setMessage(message);
        m.setCreatedAt(LocalDateTime.now());
        return repository.save(m);
    }
}
