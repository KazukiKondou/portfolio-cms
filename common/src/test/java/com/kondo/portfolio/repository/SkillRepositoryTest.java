package com.kondo.portfolio.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kondo.portfolio.entity.Skill;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class SkillRepositoryTest {

    @Autowired private SkillRepository repository;
    @Autowired private TestEntityManager em;

    private Skill skill(String name, int proficiency, int sortOrder, boolean published) {
        Skill s = new Skill();
        s.setName(name);
        s.setProficiency(proficiency);
        s.setSortOrder(sortOrder);
        s.setPublished(published);
        s.setCreatedAt(LocalDateTime.now());
        s.setUpdatedAt(LocalDateTime.now());
        return s;
    }

    @Test
    void 公開中を習熟度昇順_同習熟度はsortOrder昇順で返す() {
        em.persist(skill("Vue", 2, 2, true));
        em.persist(skill("React", 2, 1, true));
        em.persist(skill("Java", 1, 1, true));
        em.persist(skill("非公開", 1, 0, false));
        em.flush();

        List<Skill> result = repository.findByPublishedTrueOrderByProficiencyAscSortOrderAsc();

        assertThat(result).extracting(Skill::getName).containsExactly("Java", "React", "Vue");
    }

    @Test
    void 公開スキルがなければ空リスト() {
        em.persist(skill("非公開", 1, 0, false));
        em.flush();
        assertThat(repository.findByPublishedTrueOrderByProficiencyAscSortOrderAsc()).isEmpty();
    }
}
