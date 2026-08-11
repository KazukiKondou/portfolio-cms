package com.kondo.portfolio.admin.service;

import com.kondo.portfolio.entity.PageView;
import com.kondo.portfolio.repository.PageViewRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 管理画面用の閲覧数（アクセス数）参照サービス。加算は公開サイト側が行う */
@Service
public class PageViewService {

    private final PageViewRepository repository;

    public PageViewService(PageViewRepository repository) {
        this.repository = repository;
    }

    /** ページ別の閲覧数を多い順で返す */
    @Transactional(readOnly = true)
    public List<PageView> findAllByViewCountDesc() {
        return repository.findAllByOrderByViewCountDesc();
    }

    /** 全ページの合計閲覧数 */
    @Transactional(readOnly = true)
    public long totalViewCount() {
        return repository.sumViewCount();
    }
}
