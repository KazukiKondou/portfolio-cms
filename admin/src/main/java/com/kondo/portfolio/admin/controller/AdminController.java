package com.kondo.portfolio.admin.controller;

import com.kondo.portfolio.admin.service.PageViewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** 管理画面のトップ（ダッシュボード）とログイン画面 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PageViewService pageViewService;

    public AdminController(PageViewService pageViewService) {
        this.pageViewService = pageViewService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("pageViews", pageViewService.findAllByViewCountDesc());
        model.addAttribute("totalViews", pageViewService.totalViewCount());
        return "admin/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }
}
