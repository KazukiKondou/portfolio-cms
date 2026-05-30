package com.kondo.portfolio.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kondo.portfolio.entity.SiteSetting;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class SiteSettingRepositoryTest {

    @Autowired private SiteSettingRepository repository;
    @Autowired private TestEntityManager em;

    private SiteSetting setting(String key, String value) {
        SiteSetting s = new SiteSetting();
        s.setKey(key);
        s.setValue(value);
        s.setUpdatedAt(LocalDateTime.now());
        return s;
    }

    @Test
    void キーで取得できる() {
        em.persist(setting("site.title", "Kondo"));
        em.flush();
        assertThat(repository.findById("site.title"))
                .isPresent()
                .get()
                .extracting(SiteSetting::getValue)
                .isEqualTo("Kondo");
    }

    @Test
    void 存在しないキーは空() {
        assertThat(repository.findById("missing")).isEmpty();
    }

    @Test
    void 全件取得できる() {
        em.persist(setting("a", "1"));
        em.persist(setting("b", "2"));
        em.flush();
        assertThat(repository.findAll()).hasSize(2);
    }
}
