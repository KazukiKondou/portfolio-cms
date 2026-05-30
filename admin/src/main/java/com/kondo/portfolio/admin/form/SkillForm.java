package com.kondo.portfolio.admin.form;

import com.kondo.portfolio.domain.Skill;

/**
 * /admin/skills の新規作成・編集用フォーム
 */
public class SkillForm {

    private String name;
    private Integer proficiency;
    private Integer sortOrder;
    private Boolean published;

    public static SkillForm fromEntity(Skill s) {
        SkillForm f = new SkillForm();
        f.name = s.getName();
        f.proficiency = s.getProficiency();
        f.sortOrder = s.getSortOrder();
        f.published = s.getPublished();
        return f;
    }

    public void applyTo(Skill target) {
        target.setName(name);
        target.setProficiency(proficiency == null ? 2 : proficiency);
        target.setSortOrder(sortOrder == null ? 0 : sortOrder);
        target.setPublished(published != null && published);
    }

    public Skill toEntity() {
        Skill s = new Skill();
        applyTo(s);
        return s;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getProficiency() { return proficiency; }
    public void setProficiency(Integer proficiency) { this.proficiency = proficiency; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Boolean getPublished() { return published; }
    public void setPublished(Boolean published) { this.published = published; }
}
