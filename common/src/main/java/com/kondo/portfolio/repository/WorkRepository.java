package com.kondo.portfolio.repository;

import com.kondo.portfolio.domain.Work;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<Work, Long> {

    List<Work> findByPublishedTrueOrderBySortOrderAsc();
}
