package com.kondo.portfolio.admin.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.kondo.portfolio.admin.service.PageViewService;
import com.kondo.portfolio.entity.PageView;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock private PageViewService pageViewService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new AdminController(pageViewService))
                        .setViewResolvers(
                                new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"))
                        .build();
    }

    @Test
    void ダッシュボードビュー() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"));
    }

    @Test
    void ダッシュボードにページ別閲覧数と合計を渡す() throws Exception {
        PageView pv = new PageView();
        pv.setPagePath("/");
        pv.setViewCount(12L);
        when(pageViewService.findAllByViewCountDesc()).thenReturn(List.of(pv));
        when(pageViewService.totalViewCount()).thenReturn(20L);

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("pageViews", List.of(pv)))
                .andExpect(model().attribute("totalViews", 20L));
    }

    @Test
    void ログインビュー() throws Exception {
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/login"));
    }
}
