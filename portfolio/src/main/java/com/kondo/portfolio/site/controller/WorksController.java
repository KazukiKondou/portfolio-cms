package com.kondo.portfolio.site.controller;

import com.kondo.portfolio.site.service.WorkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** 作品一覧ページ /works */
@Controller
public class WorksController {

    private final WorkService workService;

    public WorksController(WorkService workService) {
        this.workService = workService;
    }

    @GetMapping("/works")
    public String index(Model model) {
        model.addAttribute("works", workService.findPublished());
        return "works";
    }
}
