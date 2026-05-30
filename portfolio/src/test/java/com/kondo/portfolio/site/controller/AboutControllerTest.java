package com.kondo.portfolio.site.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.kondo.portfolio.entity.TimelineEvent;
import com.kondo.portfolio.site.service.AboutService;
import com.kondo.portfolio.site.service.ProficiencyGroup;
import com.kondo.portfolio.site.service.SkillService;
import com.kondo.portfolio.site.service.TimelineEventService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ExtendWith(MockitoExtension.class)
class AboutControllerTest {

    @Mock private AboutService aboutService;
    @Mock private SkillService skillService;
    @Mock private TimelineEventService timelineService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(
                                new AboutController(aboutService, skillService, timelineService))
                        .setViewResolvers(
                                new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"))
                        .build();
    }

    @Test
    void aboutビューにbio段落_skill_timelineを渡す() throws Exception {
        when(aboutService.splitBioParagraphs("自己紹介")).thenReturn(List.of("自己紹介"));
        ProficiencyGroup group = new ProficiencyGroup(1, "主力", "Java");
        when(skillService.findGroupedByProficiency()).thenReturn(List.of(group));
        TimelineEvent ev = new TimelineEvent();
        when(timelineService.findPublished()).thenReturn(List.of(ev));

        mockMvc.perform(get("/about").flashAttr("settings", Map.of("about.bio", "自己紹介")))
                .andExpect(status().isOk())
                .andExpect(view().name("about"))
                .andExpect(model().attribute("aboutBioParagraphs", List.of("自己紹介")))
                .andExpect(model().attribute("proficiencyGroups", List.of(group)))
                .andExpect(model().attribute("timelineEvents", List.of(ev)));
    }
}
