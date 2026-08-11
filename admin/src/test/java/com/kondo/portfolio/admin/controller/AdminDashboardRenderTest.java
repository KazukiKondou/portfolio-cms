package com.kondo.portfolio.admin.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kondo.portfolio.entity.PageView;
import com.kondo.portfolio.repository.PageViewRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * ダッシュボードのテンプレートが実際に描画できるかを見る。
 *
 * <p>ログイン画面を通さずに中身だけ確かめたいので、セキュリティフィルタは外している。
 */
@SpringBootTest(
        properties = {
            "spring.datasource.url=jdbc:h2:mem:admin-dashboard-test;DB_CLOSE_DELAY=-1",
            "spring.flyway.enabled=false",
            "spring.jpa.hibernate.ddl-auto=create-drop"
        })
@AutoConfigureMockMvc(addFilters = false)
class AdminDashboardRenderTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private PageViewRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private PageView pv(String pagePath, long viewCount) {
        PageView p = new PageView();
        p.setPagePath(pagePath);
        p.setViewCount(viewCount);
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.of(2026, 8, 10, 22, 30));
        return p;
    }

    @Test
    void アクセス数がページ別と合計で描画される() throws Exception {
        repository.save(pv("/", 12));
        repository.save(pv("/about", 8));

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("アクセス数")))
                .andExpect(content().string(containsString("12 回")))
                .andExpect(content().string(containsString("8 回")))
                // 合計 20 回
                .andExpect(content().string(containsString("<strong>20</strong>")))
                .andExpect(content().string(containsString("2026-08-10 22:30")));
    }

    @Test
    void 記録が無ければ案内文を出す() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("まだ記録がありません")));
    }
}
