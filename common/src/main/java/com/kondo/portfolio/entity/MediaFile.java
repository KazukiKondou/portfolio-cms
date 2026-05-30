package com.kondo.portfolio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/** 管理画面からアップロードした画像ファイル */
@Entity
@Table(name = "media_files")
public class MediaFile extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String filename;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Column(name = "size_bytes", nullable = false)
    private Long sizeBytes;

    // @Lob を付けると PostgreSQL では oid (Large Object) になってしまう
    // bytea にしたいので素の byte[] のままにしておく (Hibernate 6 のデフォルトで VARBINARY/bytea)
    @Column(nullable = false)
    private byte[] data;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(Long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
