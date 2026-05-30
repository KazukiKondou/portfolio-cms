package com.kondo.portfolio.repository;

import com.kondo.portfolio.entity.Work;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<Work, Long> {

    List<Work> findByPublishedTrueOrderBySortOrderAsc();
}
