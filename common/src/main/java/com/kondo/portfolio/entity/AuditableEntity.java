package com.kondo.portfolio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/** BaseEntity に更新日時を加えた、更新も追跡するエンティティの基底クラス */
@MappedSuperclass
public abstract class AuditableEntity extends BaseEntity {

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
