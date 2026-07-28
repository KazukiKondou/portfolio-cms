package com.kondo.portfolio.site.seo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** canonical や og:url で使う絶対 URL を全ページの Model に入れる */
@ControllerAdvice
public class SeoAttributes {

    private final String baseUrl;

    public SeoAttributes(@Value("${portfolio.site.base-url}") String baseUrl) {
        // 末尾のスラッシュが残ると canonical が "//works" になるので落としておく
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    @ModelAttribute("baseUrl")
    public String baseUrl() {
        return baseUrl;
    }

    @ModelAttribute("seo")
    public SeoText seo() {
        return new SeoText();
    }
}
