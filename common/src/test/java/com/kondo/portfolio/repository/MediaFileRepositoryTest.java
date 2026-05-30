package com.kondo.portfolio.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kondo.portfolio.entity.MediaFile;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MediaFileRepositoryTest {

    @Autowired private MediaFileRepository repository;

    private MediaFile media(String filename, byte[] data) {
        MediaFile m = new MediaFile();
        m.setFilename(filename);
        m.setContentType("image/png");
        m.setSizeBytes((long) data.length);
        m.setData(data);
        m.setCreatedAt(LocalDateTime.now());
        return m;
    }

    @Test
    void 保存してバイナリ込みで取得できる() {
        byte[] data = {1, 2, 3, 4};
        MediaFile saved = repository.save(media("a.png", data));

        assertThat(saved.getId()).isNotNull();
        MediaFile found = repository.findById(saved.getId()).orElseThrow();
        assertThat(found.getData()).isEqualTo(data);
        assertThat(found.getContentType()).isEqualTo("image/png");
        assertThat(found.getSizeBytes()).isEqualTo(4L);
    }

    @Test
    void 存在しないidは空() {
        assertThat(repository.findById(999L)).isEmpty();
    }
}
