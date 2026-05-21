package com.kondo.portfolio.repository;

import com.kondo.portfolio.domain.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
}
