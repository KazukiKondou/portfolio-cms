package com.kondo.portfolio.admin.controller;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.kondo.portfolio.entity.SiteSetting;
import com.kondo.portfolio.service.SiteSettingService;
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
class AdminSettingsControllerTest {

    @Mock private SiteSettingService service;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new AdminSettingsController(service))
                        .setViewResolvers(
                                new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"))
                        .build();
    }

    @Test
    void 編集画面_全設定をフォームに詰める() throws Exception {
        SiteSetting s = new SiteSetting();
        s.setKey("site.title");
        s.setValue("Kondo");
        when(service.findAllOrdered()).thenReturn(List.of(s));

        mockMvc.perform(get("/admin/settings"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/settings/edit"))
                .andExpect(model().attributeExists("form"));
    }

    @Test
    void 保存_updateAllを呼びリダイレクト() throws Exception {
        mockMvc.perform(
                        post("/admin/settings")
                                .param("entries[0].key", "site.title")
                                .param("entries[0].value", "新タイトル"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/settings"))
                .andExpect(flash().attribute("message", "保存しました"));

        verify(service).updateAll(anyMap());
    }
}
