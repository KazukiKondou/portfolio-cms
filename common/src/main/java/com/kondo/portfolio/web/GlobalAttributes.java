package com.kondo.portfolio.web;

import com.kondo.portfolio.service.SiteSettingService;
import java.util.Map;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** 全ページ共通で Model に詰めたいもの。 ヘッダ・フッターで使う文言を全コントローラから集める手間を省く。 */
@ControllerAdvice
public class GlobalAttributes {

    private final SiteSettingService siteSettingService;

    public GlobalAttributes(SiteSettingService siteSettingService) {
        this.siteSettingService = siteSettingService;
    }

    @ModelAttribute("settings")
    public Map<String, String> settings() {
        return siteSettingService.findAllAsMap();
    }
}
