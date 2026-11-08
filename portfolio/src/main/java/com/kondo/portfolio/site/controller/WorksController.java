package com.kondo.portfolio.site.controller;

import com.kondo.portfolio.site.service.PageViewService;
import com.kondo.portfolio.site.service.WorkService;
import com.kondo.portfolio.site.shop.ShopCatalog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** 作品一覧ページ /works。CMS の作品に続けて、メルカリでの3Dプリント品販売も載せる */
@Controller
public class WorksController {

    private static final String PAGE_PATH = "/works";

    private final WorkService workService;
    private final ShopCatalog shopCatalog;
    private final PageViewService pageViewService;

    public WorksController(
            WorkService workService, ShopCatalog shopCatalog, PageViewService pageViewService) {
        this.workService = workService;
        this.shopCatalog = shopCatalog;
        this.pageViewService = pageViewService;
    }

    @GetMapping(PAGE_PATH)
    public String index(Model model) {
        pageViewService.recordView(PAGE_PATH);
        model.addAttribute("works", workService.findPublished());
        model.addAttribute("shopName", shopCatalog.shopName());
        model.addAttribute("mercariUrl", shopCatalog.mercariUrl());
        model.addAttribute("shopItems", shopCatalog.items());
        model.addAttribute("myRoles", shopCatalog.myRoles());
        return "works";
    }
}
