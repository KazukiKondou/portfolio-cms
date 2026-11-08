package com.kondo.portfolio.site.seo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SeoControllerTest {

    private final SeoController controller = new SeoController("https://example.test");

    @Test
    void robots_サイトマップの場所を示す() {
        String robots = controller.robots();

        assertThat(robots).contains("User-agent: *");
        assertThat(robots).contains("Sitemap: https://example.test/sitemap.xml");
    }

    @Test
    void sitemap_公開ページを絶対URLで並べる() {
        String xml = controller.sitemap();

        assertThat(xml).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertThat(xml).contains("<loc>https://example.test/</loc>");
        assertThat(xml).contains("<loc>https://example.test/works</loc>");
        assertThat(xml).contains("<loc>https://example.test/about</loc>");
        assertThat(xml).contains("<loc>https://example.test/contact</loc>");
        assertThat(xml).endsWith("</urlset>\n");
    }

    @Test
    void baseUrlの末尾スラッシュはURLを二重にしない() {
        SeoController withSlash = new SeoController("https://example.test/");

        assertThat(withSlash.sitemap()).contains("<loc>https://example.test/works</loc>");
        assertThat(withSlash.robots()).contains("Sitemap: https://example.test/sitemap.xml");
    }

    @Test
    void 属性としてbaseUrlを公開する() {
        assertThat(new SeoAttributes("https://example.test/").baseUrl())
                .isEqualTo("https://example.test");
    }

    @Test
    void summarize_改行を潰して1行にする() {
        SeoText seo = new SeoText();

        assertThat(seo.summarize("一行目\n\n二行目", 120)).isEqualTo("一行目 二行目");
        assertThat(seo.summarize("あいうえお", 3)).isEqualTo("あいう…");
        assertThat(seo.summarize(null, 120)).isEmpty();
        assertThat(seo.summarize("   ", 120)).isEmpty();
    }
}
