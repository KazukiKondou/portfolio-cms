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

import com.kondo.portfolio.admin.service.WorkService;
import com.kondo.portfolio.entity.Work;
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
class AdminWorksControllerTest {

    @Mock private WorkService service;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new AdminWorksController(service))
                        .setViewResolvers(
                                new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"))
                        .build();
    }

    @Test
    void 一覧() throws Exception {
        when(service.findAllOrdered()).thenReturn(List.of(new Work()));
        mockMvc.perform(get("/admin/works"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/works/index"))
                .andExpect(model().attributeExists("works"));
    }

    @Test
    void 新規フォーム() throws Exception {
        mockMvc.perform(get("/admin/works/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/works/form"))
                .andExpect(model().attribute("isNew", true))
                .andExpect(model().attributeExists("work"));
    }

    @Test
    void 編集フォーム_存在する() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(new Work()));
        mockMvc.perform(get("/admin/works/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/works/form"))
                .andExpect(model().attribute("isNew", false));
    }

    @Test
    void 編集フォーム_存在しなければリダイレクト() throws Exception {
        when(service.findById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/admin/works/99/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/works"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void 作成() throws Exception {
        mockMvc.perform(post("/admin/works").param("title", "x").param("sortOrder", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/works"))
                .andExpect(flash().attribute("message", "作成しました"));
        verify(service).save(any(Work.class));
    }

    @Test
    void 更新_成功() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(Optional.of(new Work()));
        mockMvc.perform(post("/admin/works/1").param("title", "x").param("sortOrder", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/works"))
                .andExpect(flash().attribute("message", "更新しました"));
    }

    @Test
    void 更新_対象なしはエラー() throws Exception {
        when(service.update(eq(99L), any())).thenReturn(Optional.empty());
        mockMvc.perform(post("/admin/works/99").param("title", "x").param("sortOrder", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void 削除() throws Exception {
        mockMvc.perform(post("/admin/works/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/works"))
                .andExpect(flash().attribute("message", "削除しました"));
        verify(service).deleteById(1L);
    }
}
