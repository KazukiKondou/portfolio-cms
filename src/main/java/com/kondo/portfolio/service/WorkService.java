package com.kondo.portfolio.service;

import com.kondo.portfolio.domain.Work;
import com.kondo.portfolio.repository.WorkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkService {

    private final WorkRepository repository;

    public WorkService(WorkRepository repository) {
        this.repository = repository;
    }

    /**
     * 公開中の作品を sort_order 昇順で返す
     */
    @Transactional(readOnly = true)
    public List<Work> findPublished() {
        return repository.findByPublishedTrueOrderBySortOrderAsc();
    }
}
