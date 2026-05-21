package com.kondo.portfolio.controller;

import com.kondo.portfolio.service.SiteSettingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * トップページ用のコントローラ
 */
@Controller
public class HomeController {

    private final SiteSettingService siteSettingService;

    public HomeController(SiteSettingService siteSettingService) {
        this.siteSettingService = siteSettingService;
    }

    @GetMapping("/")
    public String index(Model model) {
        // 文言は DB から取って Map でテンプレートに渡す
        model.addAttribute("settings", siteSettingService.findAllAsMap());
        return "index";
    }
}
