package com.kondo.portfolio.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * About ページ用のドメインロジック
 */
@Service
public class AboutService {

    /**
     * 自己紹介テキストを空行で段落に分割する
     */
    public List<String> splitBioParagraphs(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return Arrays.stream(text.split("\\R{2,}"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
