package com.kondo.portfolio.site.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kondo.portfolio.entity.PageView;
import com.kondo.portfolio.repository.PageViewRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.QueryTimeoutException;

@ExtendWith(MockitoExtension.class)
class PageViewServiceTest {

    @Mock private PageViewRepository repository;
    @InjectMocks private PageViewService service;

    @Test
    void 既存の行があれば加算だけして新規作成しない() {
        when(repository.incrementViewCount(eq("/"), any(LocalDateTime.class))).thenReturn(1);

        service.recordView("/");

        verify(repository).incrementViewCount(eq("/"), any(LocalDateTime.class));
        verify(repository, never()).save(any(PageView.class));
    }

    @Test
    void 行が無ければ閲覧数1で作る() {
        when(repository.incrementViewCount(eq("/new"), any(LocalDateTime.class))).thenReturn(0);

        service.recordView("/new");

        ArgumentCaptor<PageView> captor = ArgumentCaptor.forClass(PageView.class);
        verify(repository).save(captor.capture());
        PageView saved = captor.getValue();
        assertThat(saved.getPagePath()).isEqualTo("/new");
        assertThat(saved.getViewCount()).isEqualTo(1L);
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void 同時に作られて一意制約に当たったら加算し直す() {
        when(repository.incrementViewCount(eq("/new"), any(LocalDateTime.class))).thenReturn(0, 1);
        when(repository.save(any(PageView.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        service.recordView("/new");

        // 1回目は0件 → save が衝突 → もう一度加算する
        verify(repository, times(2)).incrementViewCount(eq("/new"), any(LocalDateTime.class));
    }

    @Test
    void DBエラーでも例外を投げない() {
        when(repository.incrementViewCount(eq("/"), any(LocalDateTime.class)))
                .thenThrow(new QueryTimeoutException("timeout"));

        assertThatCode(() -> service.recordView("/")).doesNotThrowAnyException();
    }
}
