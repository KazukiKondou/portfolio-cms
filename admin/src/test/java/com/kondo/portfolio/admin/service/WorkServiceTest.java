package com.kondo.portfolio.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kondo.portfolio.entity.Work;
import com.kondo.portfolio.repository.WorkRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class WorkServiceTest {

    @Mock private WorkRepository repository;
    @InjectMocks private WorkService service;

    @Test
    void findAllOrdered_sortOrderとidで並べる() {
        Work w = new Work();
        when(repository.findAll(Mockito.any(Sort.class))).thenReturn(List.of(w));
        assertThat(service.findAllOrdered()).containsExactly(w);
    }

    @Test
    void findById_委譲する() {
        Work w = new Work();
        when(repository.findById(1L)).thenReturn(Optional.of(w));
        assertThat(service.findById(1L)).containsSame(w);
    }

    @Test
    void save_新規はcreatedAtとupdatedAtを設定しデフォルトを埋める() {
        when(repository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));
        Work w = new Work();
        w.setTitle("x");
        // published/sortOrder 未設定

        Work saved = service.save(w);

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
        assertThat(saved.getPublished()).isTrue();
        assertThat(saved.getSortOrder()).isZero();
    }

    @Test
    void save_既存はcreatedAtを変えずupdatedAtのみ更新() {
        when(repository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));
        Work w = new Work();
        w.setId(5L);
        java.time.LocalDateTime created = java.time.LocalDateTime.of(2020, 1, 1, 0, 0);
        w.setCreatedAt(created);
        w.setPublished(true);
        w.setSortOrder(3);

        Work saved = service.save(w);

        assertThat(saved.getCreatedAt()).isEqualTo(created);
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void update_存在すればapplierを適用して保存() {
        Work existing = new Work();
        existing.setTitle("old");
        existing.setPublished(true);
        existing.setSortOrder(1);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<Work> result = service.update(1L, w -> w.setTitle("new"));

        assertThat(result).isPresent();
        assertThat(existing.getTitle()).isEqualTo("new");
    }

    @Test
    void update_存在しなければemptyで保存しない() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Work> result = service.update(99L, w -> w.setTitle("x"));

        assertThat(result).isEmpty();
        verify(repository, never()).save(Mockito.any());
    }

    @Test
    void deleteById_委譲する() {
        service.deleteById(1L);
        verify(repository).deleteById(1L);
    }
}
