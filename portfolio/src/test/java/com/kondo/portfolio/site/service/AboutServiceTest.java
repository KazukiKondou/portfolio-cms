package com.kondo.portfolio.site.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AboutServiceTest {

    private final AboutService service = new AboutService();

    @Test
    void nullは空リスト() {
        assertThat(service.splitBioParagraphs(null)).isEmpty();
    }

    @Test
    void 空白のみは空リスト() {
        assertThat(service.splitBioParagraphs("   ")).isEmpty();
    }

    @Test
    void 単一段落() {
        assertThat(service.splitBioParagraphs("こんにちは")).containsExactly("こんにちは");
    }

    @Test
    void 空行で複数段落に分割() {
        String text = "段落1\n\n段落2\n\n段落3";
        assertThat(service.splitBioParagraphs(text)).containsExactly("段落1", "段落2", "段落3");
    }

    @Test
    void 連続する空行は1つの区切りとして扱う() {
        String text = "段落1\n\n\n\n段落2";
        assertThat(service.splitBioParagraphs(text)).containsExactly("段落1", "段落2");
    }

    @Test
    void 各段落は前後の空白がトリムされる() {
        String text = "  段落1  \n\n  段落2  ";
        assertThat(service.splitBioParagraphs(text)).containsExactly("段落1", "段落2");
    }
}
