package com.camper.rental.entity.cms;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.camper.rental.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "cms_posts")
public class CmsPost extends BaseEntity {

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "slug", nullable = false, unique = true, length = 200)
    private String slug;

    @Column(name = "excerpt", length = 1000)
    private String excerpt;

    @Column(name = "content", nullable = false, length = 10000)
    private String content;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Column(name = "published", nullable = false)
    private boolean published;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @PrePersist
    void ensurePublicId() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
    }

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }
}
