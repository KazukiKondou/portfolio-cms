package com.kondo.portfolio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Arrays;
import java.util.List;

/** About ページの略歴 (高校卒業、大学入学、インターン等) */
@Entity
@Table(name = "timeline_events")
public class TimelineEvent extends AuditableEntity {

    // year/month は H2 予約語なので、テーブル側は event_year/event_month
    @Column(name = "event_year", nullable = false)
    private Integer year;

    /** 月不明の場合は null 可 */
    @Column(name = "event_month")
    private Integer month;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    /** " · " 区切りのタグ。例: "Java · Spring Boot" */
    @Column(length = 300)
    private String tags;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(nullable = false)
    private Boolean published;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    /** "2018年" or "2018年3月" の形で返す */
    public String getDateLabel() {
        if (year == null) return "";
        if (month == null) return year + "年";
        return year + "年" + month + "月";
    }

    /** タグを " · " で分割した List で返す。テンプレートで th:each に使う */
    public List<String> getTagList() {
        if (tags == null || tags.isBlank()) return List.of();
        return Arrays.stream(tags.split(" · "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
