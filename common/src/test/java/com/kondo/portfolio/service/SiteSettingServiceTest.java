package com.kondo.portfolio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kondo.portfolio.entity.SiteSetting;
import com.kondo.portfolio.repository.SiteSettingRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SiteSettingServiceTest {

    @Mock private SiteSettingRepository repository;
    @InjectMocks private SiteSettingService service;

    private SiteSetting setting(String key, String value) {
        SiteSetting s = new SiteSetting();
        s.setKey(key);
        s.setValue(value);
        s.setUpdatedAt(LocalDateTime.now());
        return s;
    }

    @Test
    void findAllAsMap_キーと値のMapにする() {
        when(repository.findAll())
                .thenReturn(List.of(setting("site.title", "Kondo"), setting("nav.home", "Home")));

        Map<String, String> result = service.findAllAsMap();

        assertThat(result).containsEntry("site.title", "Kondo").containsEntry("nav.home", "Home");
    }

    @Test
    void updateAll_存在するキーは値が更新される() {
        SiteSetting existing = setting("site.title", "old");
        when(repository.findById("site.title")).thenReturn(Optional.of(existing));

        service.updateAll(Map.of("site.title", "new"));

        assertThat(existing.getValue()).isEqualTo("new");
    }

    @Test
    void updateAll_存在しないキーは無視される() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        service.updateAll(Map.of("missing", "x"));

        verify(repository, never()).save(any());
    }
}
