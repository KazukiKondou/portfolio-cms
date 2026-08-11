package com.kondo.portfolio.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.kondo.portfolio.entity.PageView;
import com.kondo.portfolio.repository.PageViewRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PageViewServiceTest {

    @Mock private PageViewRepository repository;
    @InjectMocks private PageViewService service;

    @Test
    void findAllByViewCountDesc_閲覧数の多い順取得に委譲する() {
        PageView pv = new PageView();
        when(repository.findAllByOrderByViewCountDesc()).thenReturn(List.of(pv));

        assertThat(service.findAllByViewCountDesc()).containsExactly(pv);
    }

    @Test
    void totalViewCount_合計取得に委譲する() {
        when(repository.sumViewCount()).thenReturn(42L);

        assertThat(service.totalViewCount()).isEqualTo(42L);
    }
}
