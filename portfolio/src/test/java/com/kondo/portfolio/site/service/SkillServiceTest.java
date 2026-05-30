package com.kondo.portfolio.site.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.kondo.portfolio.entity.Skill;
import com.kondo.portfolio.repository.SkillRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock private SkillRepository repository;
    @InjectMocks private SkillService service;

    private Skill skill(String name, int proficiency) {
        Skill s = new Skill();
        s.setName(name);
        s.setProficiency(proficiency);
        return s;
    }

    @Test
    void 習熟度ごとにグルーピングしてラベルとドットを付ける() {
        when(repository.findByPublishedTrueOrderByProficiencyAscSortOrderAsc())
                .thenReturn(
                        List.of(
                                skill("Java", 1),
                                skill("Spring", 1),
                                skill("React", 2),
                                skill("Go", 3)));

        List<ProficiencyGroup> groups = service.findGroupedByProficiency();

        assertThat(groups).hasSize(3);
        assertThat(groups.get(0).label()).isEqualTo("主力");
        assertThat(groups.get(0).skillsJoined()).isEqualTo("Java · Spring");
        assertThat(groups.get(0).dotsString()).isEqualTo("●●●");
        assertThat(groups.get(1).label()).isEqualTo("よく使う");
        assertThat(groups.get(1).skillsJoined()).isEqualTo("React");
        assertThat(groups.get(2).label()).isEqualTo("触ったことある");
        assertThat(groups.get(2).skillsJoined()).isEqualTo("Go");
    }

    @Test
    void スキルが無ければ空リスト() {
        when(repository.findByPublishedTrueOrderByProficiencyAscSortOrderAsc())
                .thenReturn(List.of());
        assertThat(service.findGroupedByProficiency()).isEmpty();
    }
}
