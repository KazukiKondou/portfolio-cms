package com.kondo.portfolio.site.service;

import com.kondo.portfolio.entity.ContactMessage;
import com.kondo.portfolio.repository.ContactMessageRepository;
import com.kondo.portfolio.site.notify.DiscordNotifier;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 公開サイト用のお問い合わせ受信サービス */
@Service
public class ContactMessageService {

    private final ContactMessageRepository repository;
    private final DiscordNotifier notifier;

    public ContactMessageService(ContactMessageRepository repository, DiscordNotifier notifier) {
        this.repository = repository;
        this.notifier = notifier;
    }

    /** フォームから受信したメッセージを保存し、Discord に通知する */
    @Transactional
    public ContactMessage receive(String name, String email, String message) {
        ContactMessage m = new ContactMessage();
        m.setName(name);
        m.setEmail(email);
        m.setMessage(message);
        m.setCreatedAt(LocalDateTime.now());
        ContactMessage saved = repository.save(m);
        // 管理画面を見に行かなくても気付けるように通知する（別スレッドなので送信は待たされない）
        notifier.notifyReceived(saved);
        return saved;
    }
}
