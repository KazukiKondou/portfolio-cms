package com.kondo.portfolio.admin.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.kondo.portfolio.admin.service.SkillService;
import com.kondo.portfolio.entity.Skill;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ExtendWith(MockitoExtension.class)
class AdminSkillsControllerTest {

    @Mock private SkillService service;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new AdminSkillsController(service))
                        .setViewResolvers(
                                new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"))
                        .build();
    }

    @Test
    void 一覧() throws Exception {
        when(service.findAllOrdered()).thenReturn(List.of(new Skill()));
        mockMvc.perform(get("/admin/skills"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/skills/index"));
    }

    @Test
    void 新規フォーム() throws Exception {
        mockMvc.perform(get("/admin/skills/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isNew", true));
    }

    @Test
    void 編集_存在する() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(new Skill()));
        mockMvc.perform(get("/admin/skills/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isNew", false));
    }

    @Test
    void 編集_存在しない() throws Exception {
        when(service.findById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/admin/skills/99/edit"))
                .andExpect(redirectedUrl("/admin/skills"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void 作成() throws Exception {
        mockMvc.perform(post("/admin/skills").param("name", "Java").param("proficiency", "1"))
                .andExpect(redirectedUrl("/admin/skills"));
        verify(service).save(any(Skill.class));
    }

    @Test
    void 更新_成功() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(Optional.of(new Skill()));
        mockMvc.perform(post("/admin/skills/1").param("name", "Java"))
                .andExpect(flash().attribute("message", "更新しました"));
    }

    @Test
    void 更新_対象なし() throws Exception {
        when(service.update(eq(99L), any())).thenReturn(Optional.empty());
        mockMvc.perform(post("/admin/skills/99").param("name", "Java"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void 削除() throws Exception {
        mockMvc.perform(post("/admin/skills/1/delete")).andExpect(redirectedUrl("/admin/skills"));
        verify(service).deleteById(1L);
    }
}
