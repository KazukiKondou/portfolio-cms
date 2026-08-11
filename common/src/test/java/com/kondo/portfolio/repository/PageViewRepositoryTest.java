package com.kondo.portfolio.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kondo.portfolio.entity.PageView;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class PageViewRepositoryTest {

    @Autowired private PageViewRepository repository;
    @Autowired private TestEntityManager em;

    private PageView pv(String pagePath, long viewCount) {
        PageView p = new PageView();
        p.setPagePath(pagePath);
        p.setViewCount(viewCount);
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());
        return p;
    }

    @Test
    void 閲覧数を1増やして更新日時を書き換える() {
        PageView p = pv("/", 10);
        p.setUpdatedAt(LocalDateTime.of(2026, 1, 1, 0, 0));
        em.persist(p);
        em.flush();

        LocalDateTime now = LocalDateTime.of(2026, 8, 10, 12, 0);
        int updated = repository.incrementViewCount("/", now);
        em.clear();

        assertThat(updated).isEqualTo(1);
        PageView reloaded = repository.findByPagePath("/").orElseThrow();
        assertThat(reloaded.getViewCount()).isEqualTo(11L);
        assertThat(reloaded.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void 該当パスの行が無ければ0件を返す() {
        em.persist(pv("/", 1));
        em.flush();

        int updated = repository.incrementViewCount("/unknown", LocalDateTime.now());

        assertThat(updated).isZero();
    }

    @Test
    void 他のパスの閲覧数は増やさない() {
        em.persist(pv("/", 5));
        em.persist(pv("/about", 5));
        em.flush();

        repository.incrementViewCount("/about", LocalDateTime.now());
        em.clear();

        assertThat(repository.findByPagePath("/").orElseThrow().getViewCount()).isEqualTo(5L);
        assertThat(repository.findByPagePath("/about").orElseThrow().getViewCount()).isEqualTo(6L);
    }

    @Test
    void 閲覧数の多い順に返す() {
        em.persist(pv("/", 3));
        em.persist(pv("/works", 9));
        em.persist(pv("/about", 5));
        em.flush();

        assertThat(repository.findAllByOrderByViewCountDesc())
                .extracting(PageView::getPagePath)
                .containsExactly("/works", "/about", "/");
    }

    @Test
    void 全ページの合計を返す() {
        em.persist(pv("/", 3));
        em.persist(pv("/works", 9));
        em.flush();

        assertThat(repository.sumViewCount()).isEqualTo(12L);
    }

    @Test
    void 記録が無ければ合計は0() {
        assertThat(repository.sumViewCount()).isZero();
    }
}
