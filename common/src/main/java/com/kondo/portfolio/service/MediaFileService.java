package com.kondo.portfolio.service;

import com.kondo.portfolio.entity.MediaFile;
import com.kondo.portfolio.repository.MediaFileRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MediaFileService {

    private final MediaFileRepository repository;

    public MediaFileService(MediaFileRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Long save(String filename, String contentType, byte[] data) {
        MediaFile f = new MediaFile();
        f.setFilename(filename);
        f.setContentType(contentType);
        f.setSizeBytes((long) data.length);
        f.setData(data);
        f.setCreatedAt(LocalDateTime.now());
        return repository.save(f).getId();
    }

    @Transactional(readOnly = true)
    public Optional<MediaFile> findById(Long id) {
        return repository.findById(id);
    }
}
