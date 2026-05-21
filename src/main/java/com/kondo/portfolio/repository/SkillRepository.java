package com.kondo.portfolio.repository;

import com.kondo.portfolio.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByPublishedTrueOrderBySortOrderAsc();
}
