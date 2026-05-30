package com.kondo.portfolio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.kondo.portfolio.entity.MediaFile;
import com.kondo.portfolio.repository.MediaFileRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MediaFileServiceTest {

    @Mock private MediaFileRepository repository;
    @InjectMocks private MediaFileService service;

    @Test
    void save_フィールドを設定してidを返す() {
        byte[] data = {1, 2, 3};
        MediaFile persisted = new MediaFile();
        persisted.setId(42L);
        when(repository.save(org.mockito.ArgumentMatchers.any())).thenReturn(persisted);

        Long id = service.save("a.png", "image/png", data);

        assertThat(id).isEqualTo(42L);
        ArgumentCaptor<MediaFile> captor = ArgumentCaptor.forClass(MediaFile.class);
        org.mockito.Mockito.verify(repository).save(captor.capture());
        MediaFile saved = captor.getValue();
        assertThat(saved.getFilename()).isEqualTo("a.png");
        assertThat(saved.getContentType()).isEqualTo("image/png");
        assertThat(saved.getSizeBytes()).isEqualTo(3L);
        assertThat(saved.getData()).isEqualTo(data);
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void findById_リポジトリに委譲する() {
        MediaFile m = new MediaFile();
        when(repository.findById(1L)).thenReturn(Optional.of(m));
        assertThat(service.findById(1L)).containsSame(m);
    }
}
