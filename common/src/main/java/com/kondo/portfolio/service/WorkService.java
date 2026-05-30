package com.kondo.portfolio.service;

import com.kondo.portfolio.domain.Work;
import com.kondo.portfolio.repository.WorkRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class WorkService {

    private final WorkRepository repository;

    public WorkService(WorkRepository repository) {
        this.repository = repository;
    }

    /**
     * 公開中の作品を sort_order 昇順で返す（公開サイト用）
     */
    @Transactional(readOnly = true)
    public List<Work> findPublished() {
        return repository.findByPublishedTrueOrderBySortOrderAsc();
    }

    /**
     * 全件返す（管理画面用、未公開も含む）
     */
    @Transactional(readOnly = true)
    public List<Work> findAllOrdered() {
        return repository.findAll(Sort.by("sortOrder").ascending().and(Sort.by("id").ascending()));
    }

    @Transactional(readOnly = true)
    public Optional<Work> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Work save(Work work) {
        LocalDateTime now = LocalDateTime.now();
        if (work.getId() == null) {
            work.setCreatedAt(now);
        }
        work.setUpdatedAt(now);
        if (work.getPublished() == null) work.setPublished(true);
        if (work.getSortOrder() == null) work.setSortOrder(0);
        return repository.save(work);
    }

    /**
     * id で既存レコードを引いて applier で変更を当て、保存する。
     * 見つからなければ Optional.empty()。
     */
    @Transactional
    public Optional<Work> update(Long id, Consumer<Work> applier) {
        return repository.findById(id).map(existing -> {
            applier.accept(existing);
            return save(existing);
        });
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
