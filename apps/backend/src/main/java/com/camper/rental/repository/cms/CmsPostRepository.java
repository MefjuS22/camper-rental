package com.camper.rental.repository.cms;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camper.rental.entity.cms.CmsPost;

public interface CmsPostRepository extends JpaRepository<CmsPost, Long> {
    List<CmsPost> findAllByPublishedTrueOrderByPublishedAtDesc();
    Optional<CmsPost> findBySlugAndPublishedTrue(String slug);
    Optional<CmsPost> findByPublicId(UUID publicId);
    boolean existsBySlug(String slug);
    boolean existsBySlugAndPublicIdNot(String slug, UUID publicId);
}
