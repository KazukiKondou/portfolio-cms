package com.kondo.portfolio.site.service;

import com.kondo.portfolio.entity.TimelineEvent;
import com.kondo.portfolio.repository.TimelineEventRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 公開サイト用の略歴取得サービス */
@Service
public class TimelineEventService {

    private final TimelineEventRepository repository;

    public TimelineEventService(TimelineEventRepository repository) {
        this.repository = repository;
    }

    /** 公開中の略歴を古い順で返す */
    @Transactional(readOnly = true)
    public List<TimelineEvent> findPublished() {
        return repository.findPublishedInOrder();
    }
}
