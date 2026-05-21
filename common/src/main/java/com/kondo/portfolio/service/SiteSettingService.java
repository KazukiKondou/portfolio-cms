package com.kondo.portfolio.service;

import com.kondo.portfolio.domain.SiteSetting;
import com.kondo.portfolio.repository.SiteSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SiteSettingService {

    private final SiteSettingRepository repository;

    public SiteSettingService(SiteSettingRepository repository) {
        this.repository = repository;
    }

    /**
     * 全設定を key→value の Map で返す。
     * テンプレートから ${settings['site.title']} のように使う。
     */
    @Transactional(readOnly = true)
    public Map<String, String> findAllAsMap() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(SiteSetting::getKey, SiteSetting::getValue));
    }
}
