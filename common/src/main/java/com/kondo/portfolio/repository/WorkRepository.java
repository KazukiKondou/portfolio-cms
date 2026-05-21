package com.kondo.portfolio.repository;

import com.kondo.portfolio.domain.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Long> {

    List<Work> findByPublishedTrueOrderBySortOrderAsc();
}
