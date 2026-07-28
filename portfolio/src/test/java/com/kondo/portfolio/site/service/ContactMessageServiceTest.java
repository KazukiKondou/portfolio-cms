package com.kondo.portfolio.site.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.kondo.portfolio.entity.ContactMessage;
import com.kondo.portfolio.repository.ContactMessageRepository;
import com.kondo.portfolio.site.notify.DiscordNotifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactMessageServiceTest {

    @Mock private ContactMessageRepository repository;
    @Mock private DiscordNotifier notifier;
    @InjectMocks private ContactMessageService service;

    @Test
    void receive_フィールドを設定して保存する() {
        when(repository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

        service.receive("太郎", "t@example.com", "本文");

        ArgumentCaptor<ContactMessage> captor = ArgumentCaptor.forClass(ContactMessage.class);
        Mockito.verify(repository).save(captor.capture());
        ContactMessage saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("太郎");
        assertThat(saved.getEmail()).isEqualTo("t@example.com");
        assertThat(saved.getMessage()).isEqualTo("本文");
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void receive_保存したメッセージをDiscordに通知する() {
        when(repository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

        ContactMessage saved = service.receive("太郎", "t@example.com", "本文");

        Mockito.verify(notifier).notifyReceived(saved);
    }
}
