package com.kondo.portfolio.controller;

import com.kondo.portfolio.service.SkillService;
import com.kondo.portfolio.service.TimelineEventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 自己紹介ページ /about
 */
@Controller
public class AboutController {

    private final SkillService skillService;
    private final TimelineEventService timelineService;

    public AboutController(SkillService skillService, TimelineEventService timelineService) {
        this.skillService = skillService;
        this.timelineService = timelineService;
    }

    @GetMapping("/about")
    public String index(@ModelAttribute("settings") Map<String, String> settings, Model model) {
        String bio = settings.getOrDefault("about.bio", "");
        model.addAttribute("aboutBioParagraphs", splitParagraphs(bio));
        model.addAttribute("proficiencyGroups", skillService.findGroupedByProficiency());
        model.addAttribute("timelineEvents", timelineService.findPublished());
        return "about";
    }

    // 空行を境に段落に分割
    private List<String> splitParagraphs(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return Arrays.stream(text.split("\\R{2,}"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
