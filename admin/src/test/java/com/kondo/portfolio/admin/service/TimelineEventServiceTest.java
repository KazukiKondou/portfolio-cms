package com.kondo.portfolio.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kondo.portfolio.entity.TimelineEvent;
import com.kondo.portfolio.repository.TimelineEventRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimelineEventServiceTest {

    @Mock private TimelineEventRepository repository;
    @InjectMocks private TimelineEventService service;

    @Test
    void findAllOrdered_委譲する() {
        TimelineEvent e = new TimelineEvent();
        when(repository.findAllInOrder()).thenReturn(List.of(e));
        assertThat(service.findAllOrdered()).containsExactly(e);
    }

    @Test
    void findById_委譲する() {
        TimelineEvent e = new TimelineEvent();
        when(repository.findById(1L)).thenReturn(Optional.of(e));
        assertThat(service.findById(1L)).containsSame(e);
    }

    @Test
    void save_新規はデフォルトを埋める() {
        when(repository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));
        TimelineEvent e = new TimelineEvent();
        e.setYear(2025);
        e.setTitle("x");

        TimelineEvent saved = service.save(e);

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
        assertThat(saved.getPublished()).isTrue();
        assertThat(saved.getSortOrder()).isZero();
    }

    @Test
    void update_存在すれば適用() {
        TimelineEvent existing = new TimelineEvent();
        existing.setTitle("old");
        existing.setPublished(true);
        existing.setSortOrder(1);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<TimelineEvent> result = service.update(1L, e -> e.setTitle("new"));

        assertThat(result).isPresent();
        assertThat(existing.getTitle()).isEqualTo("new");
    }

    @Test
    void update_存在しなければempty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThat(service.update(99L, e -> e.setTitle("x"))).isEmpty();
        verify(repository, never()).save(Mockito.any());
    }

    @Test
    void deleteById_委譲する() {
        service.deleteById(1L);
        verify(repository).deleteById(1L);
    }
}
