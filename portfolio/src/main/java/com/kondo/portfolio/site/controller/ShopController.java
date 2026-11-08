package com.kondo.portfolio.site.controller;

import com.kondo.portfolio.site.service.PageViewService;
import com.kondo.portfolio.site.shop.ShopCatalog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** メルカリでの3Dプリント品販売を紹介するページ /shop */
@Controller
public class ShopController {

    private static final String PAGE_PATH = "/shop";

    private final ShopCatalog catalog;
    private final PageViewService pageViewService;

    public ShopController(ShopCatalog catalog, PageViewService pageViewService) {
        this.catalog = catalog;
        this.pageViewService = pageViewService;
    }

    @GetMapping(PAGE_PATH)
    public String index(Model model) {
        pageViewService.recordView(PAGE_PATH);
        model.addAttribute("shopName", catalog.shopName());
        model.addAttribute("mercariUrl", catalog.mercariUrl());
        model.addAttribute("shopItems", catalog.items());
        model.addAttribute("myRoles", catalog.myRoles());
        return "shop";
    }
}
