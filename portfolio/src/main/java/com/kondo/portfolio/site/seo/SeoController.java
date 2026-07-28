package com.kondo.portfolio.site.seo;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** クローラ向けの robots.txt と sitemap.xml を返す */
@RestController
public class SeoController {

    /** サイトマップに載せる公開ページ */
    private static final List<String> PUBLIC_PATHS = List.of("/", "/works", "/about", "/contact");

    private final String baseUrl;

    public SeoController(@Value("${portfolio.site.base-url}") String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String robots() {
        return """
                User-agent: *
                Allow: /

                Sitemap: %s/sitemap.xml
                """
                .formatted(baseUrl);
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemap() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        for (String path : PUBLIC_PATHS) {
            xml.append("  <url><loc>").append(baseUrl).append(path).append("</loc></url>\n");
        }
        xml.append("</urlset>\n");
        return xml.toString();
    }
}
