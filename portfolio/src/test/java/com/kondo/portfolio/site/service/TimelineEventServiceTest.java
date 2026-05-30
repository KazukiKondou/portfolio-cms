package com.kondo.portfolio.site.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.kondo.portfolio.entity.TimelineEvent;
import com.kondo.portfolio.repository.TimelineEventRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimelineEventServiceTest {

    @Mock private TimelineEventRepository repository;
    @InjectMocks private TimelineEventService service;

    @Test
    void findPublished_公開取得に委譲する() {
        TimelineEvent e = new TimelineEvent();
        when(repository.findPublishedInOrder()).thenReturn(List.of(e));
        assertThat(service.findPublished()).containsExactly(e);
    }
}
