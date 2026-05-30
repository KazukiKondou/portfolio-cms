package com.kondo.portfolio.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kondo.portfolio.entity.Skill;
import com.kondo.portfolio.repository.SkillRepository;
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
class SkillServiceTest {

    @Mock private SkillRepository repository;
    @InjectMocks private SkillService service;

    @Test
    void findAllOrdered_委譲する() {
        Skill s = new Skill();
        when(repository.findAll(Mockito.any(Sort.class))).thenReturn(List.of(s));
        assertThat(service.findAllOrdered()).containsExactly(s);
    }

    @Test
    void findById_委譲する() {
        Skill s = new Skill();
        when(repository.findById(1L)).thenReturn(Optional.of(s));
        assertThat(service.findById(1L)).containsSame(s);
    }

    @Test
    void save_新規はデフォルトを埋める_習熟度は2() {
        when(repository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));
        Skill s = new Skill();
        s.setName("x");

        Skill saved = service.save(s);

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
        assertThat(saved.getPublished()).isTrue();
        assertThat(saved.getSortOrder()).isZero();
        assertThat(saved.getProficiency()).isEqualTo(2);
    }

    @Test
    void update_存在すれば適用() {
        Skill existing = new Skill();
        existing.setName("old");
        existing.setPublished(true);
        existing.setSortOrder(1);
        existing.setProficiency(1);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<Skill> result = service.update(1L, s -> s.setName("new"));

        assertThat(result).isPresent();
        assertThat(existing.getName()).isEqualTo("new");
    }

    @Test
    void update_存在しなければempty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThat(service.update(99L, s -> s.setName("x"))).isEmpty();
        verify(repository, never()).save(Mockito.any());
    }

    @Test
    void deleteById_委譲する() {
        service.deleteById(1L);
        verify(repository).deleteById(1L);
    }
}
