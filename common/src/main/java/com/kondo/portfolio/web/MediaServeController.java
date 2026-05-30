package com.kondo.portfolio.web;

import com.kondo.portfolio.service.MediaFileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/** /media/{id} で DB に保存された画像を配信する。 :common にあるので :site / :admin の両方で動く（site で公開用、admin でプレビュー用） */
@RestController
public class MediaServeController {

    private final MediaFileService service;

    public MediaServeController(MediaFileService service) {
        this.service = service;
    }

    @GetMapping("/media/{id}")
    public ResponseEntity<byte[]> get(@PathVariable Long id) {
        return service.findById(id)
                .map(
                        file ->
                                ResponseEntity.ok()
                                        .contentType(
                                                MediaType.parseMediaType(file.getContentType()))
                                        .body(file.getData()))
                .orElse(ResponseEntity.notFound().build());
    }
}
