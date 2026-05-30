package com.kondo.portfolio.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kondo.portfolio.entity.ContactMessage;
import com.kondo.portfolio.repository.ContactMessageRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactMessageServiceTest {

    @Mock private ContactMessageRepository repository;
    @InjectMocks private ContactMessageService service;

    @Test
    void findAllRecentFirst_委譲する() {
        ContactMessage m = new ContactMessage();
        when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(m));
        assertThat(service.findAllRecentFirst()).containsExactly(m);
    }

    @Test
    void findById_委譲する() {
        ContactMessage m = new ContactMessage();
        when(repository.findById(1L)).thenReturn(Optional.of(m));
        assertThat(service.findById(1L)).containsSame(m);
    }

    @Test
    void markAsRead_存在すればreadAtを設定() {
        ContactMessage m = new ContactMessage();
        when(repository.findById(1L)).thenReturn(Optional.of(m));

        service.markAsRead(1L);

        assertThat(m.getReadAt()).isNotNull();
        assertThat(m.isUnread()).isFalse();
    }

    @Test
    void markAsRead_存在しなければ何もしない() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        service.markAsRead(99L); // 例外が出ないこと
    }

    @Test
    void deleteById_委譲する() {
        service.deleteById(1L);
        verify(repository).deleteById(1L);
    }
}
