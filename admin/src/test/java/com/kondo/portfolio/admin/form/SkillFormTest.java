package com.kondo.portfolio.admin.form;

import static org.assertj.core.api.Assertions.assertThat;

import com.kondo.portfolio.entity.Skill;
import org.junit.jupiter.api.Test;

class SkillFormTest {

    @Test
    void fromEntity_コピーする() {
        Skill s = new Skill();
        s.setId(1L);
        s.setName("Java");
        s.setProficiency(1);
        s.setSortOrder(2);
        s.setPublished(true);

        SkillForm f = SkillForm.fromEntity(s);

        assertThat(f.getId()).isEqualTo(1L);
        assertThat(f.getName()).isEqualTo("Java");
        assertThat(f.getProficiency()).isEqualTo(1);
        assertThat(f.getSortOrder()).isEqualTo(2);
        assertThat(f.getPublished()).isTrue();
    }

    @Test
    void applyTo_proficiencyがnullなら2_sortOrderがnullなら0_publishedがnullならfalse() {
        SkillForm f = new SkillForm();
        f.setName("x");
        f.setProficiency(null);
        f.setSortOrder(null);
        f.setPublished(null);

        Skill s = new Skill();
        f.applyTo(s);

        assertThat(s.getName()).isEqualTo("x");
        assertThat(s.getProficiency()).isEqualTo(2);
        assertThat(s.getSortOrder()).isZero();
        assertThat(s.getPublished()).isFalse();
    }

    @Test
    void toEntity_新しいエンティティを作る() {
        SkillForm f = new SkillForm();
        f.setName("Java");
        assertThat(f.toEntity().getName()).isEqualTo("Java");
    }
}
