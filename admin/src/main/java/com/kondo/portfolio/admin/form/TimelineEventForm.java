package com.kondo.portfolio.admin.form;

import com.kondo.portfolio.domain.TimelineEvent;

/**
 * /admin/timeline の新規作成・編集用フォーム
 */
public class TimelineEventForm {

    private Integer year;
    private Integer month;
    private String title;
    private String description;
    private String tags;
    private Integer sortOrder;
    private Boolean published;

    public static TimelineEventForm fromEntity(TimelineEvent e) {
        TimelineEventForm f = new TimelineEventForm();
        f.year = e.getYear();
        f.month = e.getMonth();
        f.title = e.getTitle();
        f.description = e.getDescription();
        f.tags = e.getTags();
        f.sortOrder = e.getSortOrder();
        f.published = e.getPublished();
        return f;
    }

    public void applyTo(TimelineEvent target) {
        target.setYear(year);
        target.setMonth(month);
        target.setTitle(title);
        target.setDescription(description);
        target.setTags(tags);
        target.setSortOrder(sortOrder == null ? 0 : sortOrder);
        target.setPublished(published != null && published);
    }

    public TimelineEvent toEntity() {
        TimelineEvent e = new TimelineEvent();
        applyTo(e);
        return e;
    }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Boolean getPublished() { return published; }
    public void setPublished(Boolean published) { this.published = published; }
}
