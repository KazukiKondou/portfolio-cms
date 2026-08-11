package com.kondo.portfolio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/** ページごとの閲覧数。公開サイトには表示せず、集計だけを貯める */
@Entity
@Table(name = "page_views")
public class PageView extends AuditableEntity {

    /** 集計対象のパス。例: "/" "/about" */
    @Column(name = "page_path", nullable = false, unique = true, length = 200)
    private String pagePath;

    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}
