package com.kondo.portfolio.site.seo;

/** meta description 用に本文を1行へ整えるヘルパー。テンプレートから ${seo.summarize(...)} で使う */
public class SeoText {

    private static final String ELLIPSIS = "…";

    /** 改行や連続する空白を潰して1行にし、長すぎるものは切り詰める */
    public String summarize(String text, int maxLength) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String oneLine = text.replaceAll("\\s+", " ").trim();
        if (oneLine.length() <= maxLength) {
            return oneLine;
        }
        return oneLine.substring(0, maxLength) + ELLIPSIS;
    }
}
