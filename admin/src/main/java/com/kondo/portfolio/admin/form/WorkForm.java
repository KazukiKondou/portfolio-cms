package com.kondo.portfolio.admin.form;

import com.kondo.portfolio.domain.Work;

/**
 * /admin/works の新規作成・編集用フォーム
 */
public class WorkForm {

    private String title;
    private String summary;
    private String description;
    private String url;
    private String tags;
    private Integer sortOrder;
    private Boolean published;

    public static WorkForm fromEntity(Work w) {
        WorkForm f = new WorkForm();
        f.title = w.getTitle();
        f.summary = w.getSummary();
        f.description = w.getDescription();
        f.url = w.getUrl();
        f.tags = w.getTags();
        f.sortOrder = w.getSortOrder();
        f.published = w.getPublished();
        return f;
    }

    /** 既存エンティティに本フォームの値を反映 */
    public void applyTo(Work target) {
        target.setTitle(title);
        target.setSummary(summary);
        target.setDescription(description);
        target.setUrl(url);
        target.setTags(tags);
        target.setSortOrder(sortOrder == null ? 0 : sortOrder);
        target.setPublished(published != null && published);
    }

    /** 新規作成用に新しいエンティティを作る */
    public Work toEntity() {
        Work w = new Work();
        applyTo(w);
        return w;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Boolean getPublished() { return published; }
    public void setPublished(Boolean published) { this.published = published; }
}
