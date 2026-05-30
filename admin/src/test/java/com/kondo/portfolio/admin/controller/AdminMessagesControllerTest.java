package com.kondo.portfolio.admin.controller;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.kondo.portfolio.admin.service.ContactMessageService;
import com.kondo.portfolio.entity.ContactMessage;
import java.time.LocalDateTime;
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
class AdminMessagesControllerTest {

    @Mock private ContactMessageService service;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new AdminMessagesController(service))
                        .setViewResolvers(
                                new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"))
                        .build();
    }

    @Test
    void 一覧() throws Exception {
        when(service.findAllRecentFirst()).thenReturn(List.of(new ContactMessage()));
        mockMvc.perform(get("/admin/messages"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/messages/index"))
                .andExpect(model().attributeExists("messages"));
    }

    @Test
    void 詳細_未読なら既読化する() throws Exception {
        ContactMessage m = new ContactMessage(); // readAt null = 未読
        when(service.findById(1L)).thenReturn(Optional.of(m));

        mockMvc.perform(get("/admin/messages/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/messages/show"))
                .andExpect(model().attribute("message", m));

        verify(service).markAsRead(1L);
    }

    @Test
    void 詳細_既読済みは既読化しない() throws Exception {
        ContactMessage m = new ContactMessage();
        m.setReadAt(LocalDateTime.now()); // 既読
        when(service.findById(1L)).thenReturn(Optional.of(m));

        mockMvc.perform(get("/admin/messages/1")).andExpect(status().isOk());

        verify(service, never()).markAsRead(1L);
    }

    @Test
    void 詳細_存在しなければリダイレクト() throws Exception {
        when(service.findById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/admin/messages/99"))
                .andExpect(redirectedUrl("/admin/messages"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void 削除() throws Exception {
        mockMvc.perform(post("/admin/messages/1/delete"))
                .andExpect(redirectedUrl("/admin/messages"))
                .andExpect(flash().attribute("message", "削除しました"));
        verify(service).deleteById(1L);
    }
}
