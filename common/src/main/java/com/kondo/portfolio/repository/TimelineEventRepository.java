package com.kondo.portfolio.repository;

import com.kondo.portfolio.entity.TimelineEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TimelineEventRepository extends JpaRepository<TimelineEvent, Long> {

    /** 公開中、古い順 */
    @Query(
            "SELECT t FROM TimelineEvent t WHERE t.published = true "
                    + "ORDER BY t.year ASC, COALESCE(t.month, 0) ASC, t.sortOrder ASC")
    List<TimelineEvent> findPublishedInOrder();

    /** 全件、古い順 (管理画面用) */
    @Query(
            "SELECT t FROM TimelineEvent t "
                    + "ORDER BY t.year ASC, COALESCE(t.month, 0) ASC, t.sortOrder ASC")
    List<TimelineEvent> findAllInOrder();
}
