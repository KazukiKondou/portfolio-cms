package com.kondo.portfolio.repository;

import com.kondo.portfolio.entity.Skill;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByPublishedTrueOrderByProficiencyAscSortOrderAsc();
}
