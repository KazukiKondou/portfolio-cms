package com.kondo.portfolio.service;

import com.kondo.portfolio.domain.SiteSetting;
import com.kondo.portfolio.repository.SiteSettingRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SiteSettingService {

    private final SiteSettingRepository repository;

    public SiteSettingService(SiteSettingRepository repository) {
        this.repository = repository;
    }

    /** 全設定を key→value の Map で返す。 テンプレートから ${settings['site.title']} のように使う。 */
    @Transactional(readOnly = true)
    public Map<String, String> findAllAsMap() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(SiteSetting::getKey, SiteSetting::getValue));
    }

    /** 管理画面の編集用に key 昇順で返す */
    @Transactional(readOnly = true)
    public List<SiteSetting> findAllOrdered() {
        return repository.findAll(Sort.by("key"));
    }

    /** 編集された値を一括で保存 */
    @Transactional
    public void updateAll(Map<String, String> updates) {
        LocalDateTime now = LocalDateTime.now();
        for (Map.Entry<String, String> e : updates.entrySet()) {
            repository
                    .findById(e.getKey())
                    .ifPresent(
                            setting -> {
                                setting.setValue(e.getValue());
                                setting.setUpdatedAt(now);
                            });
        }
    }
}
