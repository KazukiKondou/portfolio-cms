package com.kondo.portfolio.repository;

import com.kondo.portfolio.entity.PageView;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PageViewRepository extends JpaRepository<PageView, Long> {

    Optional<PageView> findByPagePath(String pagePath);

    /** 閲覧数の多い順（管理画面の一覧用） */
    List<PageView> findAllByOrderByViewCountDesc();

    /** 全ページの合計閲覧数。1件も無ければ 0 */
    @Query("SELECT COALESCE(SUM(p.viewCount), 0) FROM PageView p")
    long sumViewCount();

    /**
     * 該当パスの閲覧数を 1 増やす。
     *
     * <p>読んでから書くと同時アクセス時にカウントを取りこぼすので、DB 側で加算する。
     *
     * @return 更新できた件数。行が無ければ 0
     */
    @Transactional
    @Modifying
    @Query(
            "UPDATE PageView p SET p.viewCount = p.viewCount + 1, p.updatedAt = :now "
                    + "WHERE p.pagePath = :pagePath")
    int incrementViewCount(@Param("pagePath") String pagePath, @Param("now") LocalDateTime now);
}
