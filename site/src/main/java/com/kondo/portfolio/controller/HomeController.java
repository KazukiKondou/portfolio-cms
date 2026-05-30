package com.kondo.portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** トップページ */
@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
