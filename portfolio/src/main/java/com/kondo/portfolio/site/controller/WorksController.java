package com.kondo.portfolio.site.controller;

import com.kondo.portfolio.site.service.PageViewService;
import com.kondo.portfolio.site.service.WorkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** 作品一覧ページ /works */
@Controller
public class WorksController {

    private static final String PAGE_PATH = "/works";

    private final WorkService workService;
    private final PageViewService pageViewService;

    public WorksController(WorkService workService, PageViewService pageViewService) {
        this.workService = workService;
        this.pageViewService = pageViewService;
    }

    @GetMapping(PAGE_PATH)
    public String index(Model model) {
        pageViewService.recordView(PAGE_PATH);
        model.addAttribute("works", workService.findPublished());
        return "works";
    }
}
