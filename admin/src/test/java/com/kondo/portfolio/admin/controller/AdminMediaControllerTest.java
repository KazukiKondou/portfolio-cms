package com.kondo.portfolio.admin.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.kondo.portfolio.service.MediaFileService;
import com.kondo.portfolio.service.SiteSettingService;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ExtendWith(MockitoExtension.class)
class AdminMediaControllerTest {

    @Mock private MediaFileService mediaService;
    @Mock private SiteSettingService settingService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(
                                new AdminMediaController(mediaService, settingService))
                        .setViewResolvers(
                                new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"))
                        .build();
    }

    @Test
    void フォーム表示_現在の写真URLをモデルに入れる() throws Exception {
        mockMvc.perform(
                        get("/admin/media")
                                .flashAttr("settings", Map.of("hero.photoUrl", "/media/1")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/media/edit"))
                .andExpect(model().attribute("currentPhotoUrl", "/media/1"));
    }

    @Test
    void 画像アップロード_保存して設定を更新() throws Exception {
        MockMultipartFile file =
                new MockMultipartFile("file", "p.png", "image/png", new byte[] {1, 2, 3});
        when(mediaService.save(eq("p.png"), eq("image/png"), any())).thenReturn(7L);

        mockMvc.perform(multipart("/admin/media").file(file))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/media"))
                .andExpect(flash().attribute("message", "アップロードしました"));

        verify(mediaService).save(eq("p.png"), eq("image/png"), any());
        verify(settingService).updateAll(anyMap());
    }

    @Test
    void 空ファイルはエラー() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "", "image/png", new byte[0]);
        mockMvc.perform(multipart("/admin/media").file(file))
                .andExpect(redirectedUrl("/admin/media"))
                .andExpect(flash().attribute("error", "ファイルを選択してください"));
        verify(mediaService, never()).save(any(), any(), any());
    }

    @Test
    void 画像以外はエラー() throws Exception {
        MockMultipartFile file =
                new MockMultipartFile("file", "a.txt", "text/plain", new byte[] {1});
        mockMvc.perform(multipart("/admin/media").file(file))
                .andExpect(redirectedUrl("/admin/media"))
                .andExpect(flash().attribute("error", "画像ファイルを指定してください"));
        verify(mediaService, never()).save(any(), any(), any());
    }
}
