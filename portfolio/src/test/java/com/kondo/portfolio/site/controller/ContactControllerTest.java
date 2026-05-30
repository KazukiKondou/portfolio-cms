package com.kondo.portfolio.site.controller;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.kondo.portfolio.site.service.ContactMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {

    @Mock private ContactMessageService service;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new ContactController(service))
                        .setViewResolvers(
                                new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"))
                        .build();
    }

    @Test
    void GET_contactでフォームを表示() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("contactForm"));
    }

    @Test
    void POST_正常入力で受信してリダイレクト() throws Exception {
        mockMvc.perform(
                        post("/contact")
                                .param("name", "太郎")
                                .param("email", "t@example.com")
                                .param("message", "本文"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact"));

        verify(service).receive("太郎", "t@example.com", "本文");
    }

    @Test
    void POST_空入力はバリデーションエラーでフォームに戻る() throws Exception {
        mockMvc.perform(
                        post("/contact")
                                .param("name", "")
                                .param("email", "invalid")
                                .param("message", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeHasErrors("contactForm"));

        verify(service, never())
                .receive(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }
}
