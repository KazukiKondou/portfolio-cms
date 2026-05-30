package com.kondo.portfolio.controller;

import com.kondo.portfolio.service.AboutService;
import com.kondo.portfolio.service.SkillService;
import com.kondo.portfolio.service.TimelineEventService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/** 自己紹介ページ /about */
@Controller
public class AboutController {

    private final AboutService aboutService;
    private final SkillService skillService;
    private final TimelineEventService timelineService;

    public AboutController(
            AboutService aboutService,
            SkillService skillService,
            TimelineEventService timelineService) {
        this.aboutService = aboutService;
        this.skillService = skillService;
        this.timelineService = timelineService;
    }

    @GetMapping("/about")
    public String index(@ModelAttribute("settings") Map<String, String> settings, Model model) {
        String bio = settings.getOrDefault("about.bio", "");
        model.addAttribute("aboutBioParagraphs", aboutService.splitBioParagraphs(bio));
        model.addAttribute("proficiencyGroups", skillService.findGroupedByProficiency());
        model.addAttribute("timelineEvents", timelineService.findPublished());
        return "about";
    }
}
