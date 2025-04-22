package com.synchrony.photo_service.models.images;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "image", indexes = {
    @Index(name = "idx_user_id", columnList = "userId")
})
public class Image implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @NotNull
    private UUID userId;

    @NotNull
    private String imgurImageId;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    protected Image() {
    }

    public Image(UUID id, UUID userId, String imgurImageId, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.imgurImageId = imgurImageId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getImgurImageId() {
        return imgurImageId;
    }

    public void setImgurImageId(String imgurImageId) {
        this.imgurImageId = imgurImageId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}