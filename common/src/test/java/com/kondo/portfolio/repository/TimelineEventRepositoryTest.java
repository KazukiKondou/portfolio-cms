package com.kondo.portfolio.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kondo.portfolio.entity.TimelineEvent;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class TimelineEventRepositoryTest {

    @Autowired private TimelineEventRepository repository;
    @Autowired private TestEntityManager em;

    private TimelineEvent ev(
            Integer year, Integer month, String title, int sortOrder, boolean published) {
        TimelineEvent e = new TimelineEvent();
        e.setYear(year);
        e.setMonth(month);
        e.setTitle(title);
        e.setSortOrder(sortOrder);
        e.setPublished(published);
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        return e;
    }

    @Test
    void 公開中を年月昇順で返す_月nullは0扱い() {
        em.persist(ev(2020, 4, "c", 0, true));
        em.persist(ev(2018, null, "a", 0, true)); // 月null → 0扱いで最古
        em.persist(ev(2018, 4, "b", 0, true));
        em.persist(ev(2019, 1, "非公開", 0, false));
        em.flush();

        List<TimelineEvent> result = repository.findPublishedInOrder();

        assertThat(result).extracting(TimelineEvent::getTitle).containsExactly("a", "b", "c");
    }

    @Test
    void 全件を年月昇順で返す_未公開も含む() {
        em.persist(ev(2020, 1, "新しい", 0, true));
        em.persist(ev(2018, 1, "古い非公開", 0, false));
        em.flush();

        List<TimelineEvent> result = repository.findAllInOrder();

        assertThat(result).extracting(TimelineEvent::getTitle).containsExactly("古い非公開", "新しい");
    }

    @Test
    void 同年月はsortOrder昇順() {
        em.persist(ev(2020, 1, "second", 2, true));
        em.persist(ev(2020, 1, "first", 1, true));
        em.flush();

        assertThat(repository.findPublishedInOrder())
                .extracting(TimelineEvent::getTitle)
                .containsExactly("first", "second");
    }
}
