package com.kondo.portfolio.site.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.kondo.portfolio.entity.Work;
import com.kondo.portfolio.repository.WorkRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkServiceTest {

    @Mock private WorkRepository repository;
    @InjectMocks private WorkService service;

    @Test
    void findPublished_リポジトリの公開取得に委譲する() {
        Work w = new Work();
        when(repository.findByPublishedTrueOrderBySortOrderAsc()).thenReturn(List.of(w));
        assertThat(service.findPublished()).containsExactly(w);
    }
}
