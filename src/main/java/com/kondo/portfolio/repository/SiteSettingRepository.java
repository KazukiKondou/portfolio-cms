package com.kondo.portfolio.repository;

import com.kondo.portfolio.domain.SiteSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteSettingRepository extends JpaRepository<SiteSetting, String> {
}
