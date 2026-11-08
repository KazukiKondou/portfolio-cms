package com.kondo.portfolio.site.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.kondo.portfolio.entity.Work;
import com.kondo.portfolio.site.service.PageViewService;
import com.kondo.portfolio.site.service.WorkService;
import com.kondo.portfolio.site.shop.ShopCatalog;
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
class WorksControllerTest {

    @Mock private WorkService workService;
    @Mock private PageViewService pageViewService;
    private final ShopCatalog catalog = new ShopCatalog();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(
                                new WorksController(workService, catalog, pageViewService))
                        .setViewResolvers(
                                new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"))
                        .build();
    }

    @Test
    void worksビューに公開作品を渡す() throws Exception {
        Work w = new Work();
        when(workService.findPublished()).thenReturn(List.of(w));

        mockMvc.perform(get("/works"))
                .andExpect(status().isOk())
                .andExpect(view().name("works"))
                .andExpect(model().attribute("works", List.of(w)));
    }

    @Test
    void worksビューにメルカリ販売の商品と役割とショップ情報を渡す() throws Exception {
        mockMvc.perform(get("/works"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("shopItems", catalog.items()))
                .andExpect(model().attribute("myRoles", catalog.myRoles()))
                .andExpect(model().attribute("shopName", catalog.shopName()))
                .andExpect(model().attribute("mercariUrl", catalog.mercariUrl()));
    }

    @Test
    void works表示で閲覧数を加算する() throws Exception {
        mockMvc.perform(get("/works")).andExpect(status().isOk());

        verify(pageViewService).recordView("/works");
    }
}
