package com.kondo.portfolio.repository;

import com.kondo.portfolio.entity.SiteSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteSettingRepository extends JpaRepository<SiteSetting, String> {}
