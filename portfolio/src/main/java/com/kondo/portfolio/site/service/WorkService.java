package com.kondo.portfolio.site.service;

import com.kondo.portfolio.entity.Work;
import com.kondo.portfolio.repository.WorkRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 公開サイト用の作品取得サービス */
@Service
public class WorkService {

    private final WorkRepository repository;

    public WorkService(WorkRepository repository) {
        this.repository = repository;
    }

    /** 公開中の作品を sort_order 昇順で返す */
    @Transactional(readOnly = true)
    public List<Work> findPublished() {
        return repository.findByPublishedTrueOrderBySortOrderAsc();
    }
}
