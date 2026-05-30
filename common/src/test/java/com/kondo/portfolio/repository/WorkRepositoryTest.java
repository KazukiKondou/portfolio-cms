package com.kondo.portfolio.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kondo.portfolio.entity.Work;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class WorkRepositoryTest {

    @Autowired private WorkRepository repository;
    @Autowired private TestEntityManager em;

    private Work work(String title, int sortOrder, boolean published) {
        Work w = new Work();
        w.setTitle(title);
        w.setSortOrder(sortOrder);
        w.setPublished(published);
        w.setCreatedAt(LocalDateTime.now());
        w.setUpdatedAt(LocalDateTime.now());
        return w;
    }

    @Test
    void 公開中のみをsortOrder昇順で返す() {
        em.persist(work("b", 2, true));
        em.persist(work("a", 1, true));
        em.persist(work("非公開", 0, false));
        em.flush();

        List<Work> result = repository.findByPublishedTrueOrderBySortOrderAsc();

        assertThat(result).extracting(Work::getTitle).containsExactly("a", "b");
    }

    @Test
    void 公開作品がなければ空リスト() {
        em.persist(work("非公開", 1, false));
        em.flush();
        assertThat(repository.findByPublishedTrueOrderBySortOrderAsc()).isEmpty();
    }

    @Test
    void 保存して採番されてfindByIdで取得できる() {
        Work saved = repository.save(work("x", 1, true));
        assertThat(saved.getId()).isNotNull();
        assertThat(repository.findById(saved.getId())).isPresent();
    }

    @Test
    void 削除できる() {
        Work saved = repository.save(work("x", 1, true));
        repository.deleteById(saved.getId());
        assertThat(repository.findById(saved.getId())).isEmpty();
    }
}
