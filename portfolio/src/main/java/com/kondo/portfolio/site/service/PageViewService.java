package com.kondo.portfolio.site.service;

import com.kondo.portfolio.entity.PageView;
import com.kondo.portfolio.repository.PageViewRepository;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * 公開サイトの閲覧数を記録するサービス。
 *
 * <p>サイト上には一切表示しない。集計に失敗してもページ表示は止めない（ログだけ残す）。
 */
@Service
public class PageViewService {

    private static final Logger log = LoggerFactory.getLogger(PageViewService.class);

    private final PageViewRepository repository;

    public PageViewService(PageViewRepository repository) {
        this.repository = repository;
    }

    /** 指定パスの閲覧数を 1 増やす。行が無ければ作る */
    public void recordView(String pagePath) {
        try {
            if (repository.incrementViewCount(pagePath, LocalDateTime.now()) == 0) {
                createFirstView(pagePath);
            }
        } catch (RuntimeException e) {
            // カウントのために画面を落とさない
            log.error("閲覧数の記録に失敗した: path={}", pagePath, e);
        }
    }

    /** マイグレーションに無いパスを初めて数えるとき用 */
    private void createFirstView(String pagePath) {
        LocalDateTime now = LocalDateTime.now();
        PageView pageView = new PageView();
        pageView.setPagePath(pagePath);
        pageView.setViewCount(1L);
        pageView.setCreatedAt(now);
        pageView.setUpdatedAt(now);
        try {
            repository.save(pageView);
        } catch (DataIntegrityViolationException e) {
            // 同時アクセスで別リクエストが先に作った場合。作られた行に加算し直す
            repository.incrementViewCount(pagePath, now);
        }
    }
}
