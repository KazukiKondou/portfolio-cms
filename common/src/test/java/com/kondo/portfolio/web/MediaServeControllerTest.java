package com.kondo.portfolio.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kondo.portfolio.entity.MediaFile;
import com.kondo.portfolio.service.MediaFileService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class MediaServeControllerTest {

    @Mock private MediaFileService service;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MediaServeController(service)).build();
    }

    @Test
    void 存在する画像をcontentTypeとバイナリで返す() throws Exception {
        MediaFile m = new MediaFile();
        m.setContentType("image/png");
        m.setData(new byte[] {1, 2, 3});
        when(service.findById(1L)).thenReturn(Optional.of(m));

        mockMvc.perform(get("/media/1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/png"))
                .andExpect(content().bytes(new byte[] {1, 2, 3}));
    }

    @Test
    void 存在しない画像は404() throws Exception {
        when(service.findById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/media/99")).andExpect(status().isNotFound());
    }
}
