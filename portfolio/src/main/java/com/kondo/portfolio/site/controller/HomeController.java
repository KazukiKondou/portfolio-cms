package com.kondo.portfolio.site.controller;

import com.kondo.portfolio.site.service.PageViewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** トップページ */
@Controller
public class HomeController {

    private static final String PAGE_PATH = "/";

    private final PageViewService pageViewService;

    public HomeController(PageViewService pageViewService) {
        this.pageViewService = pageViewService;
    }

    @GetMapping(PAGE_PATH)
    public String index() {
        pageViewService.recordView(PAGE_PATH);
        return "index";
    }
}
