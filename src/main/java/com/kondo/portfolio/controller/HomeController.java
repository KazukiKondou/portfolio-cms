package com.kondo.portfolio.controller;

import com.kondo.portfolio.service.SiteSettingService;
import com.kondo.portfolio.service.WorkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * トップページ用のコントローラ
 */
@Controller
public class HomeController {

    private final SiteSettingService siteSettingService;
    private final WorkService workService;

    public HomeController(SiteSettingService siteSettingService, WorkService workService) {
        this.siteSettingService = siteSettingService;
        this.workService = workService;
    }

    @GetMapping("/")
    public String index(Model model) {
        // 文言は DB から取って Map でテンプレートに渡す
        model.addAttribute("settings", siteSettingService.findAllAsMap());
        model.addAttribute("works", workService.findPublished());
        return "index";
    }
}
