package com.camper.rental.service.cms;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.camper.rental.dto.cms.CmsPostDto;
import com.camper.rental.dto.cms.UpsertCmsPostRequestDto;
import com.camper.rental.entity.cms.CmsPost;
import com.camper.rental.exception.BusinessLogicException;
import com.camper.rental.exception.ResourceNotFoundException;
import com.camper.rental.mapper.cms.CmsPostMapper;
import com.camper.rental.repository.cms.CmsPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CmsPostService {

    private final CmsPostRepository cmsPostRepository;
    private final CmsPostMapper cmsPostMapper;

    @Transactional(readOnly = true)
    public List<CmsPostDto> getPublishedPosts() {
        return cmsPostRepository.findAllByPublishedTrueOrderByPublishedAtDesc().stream()
            .map(cmsPostMapper::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public CmsPostDto getPublishedBySlug(String slug) {
        CmsPost post = cmsPostRepository.findBySlugAndPublishedTrue(slug)
            .orElseThrow(() -> new ResourceNotFoundException("CMS post not found for slug: " + slug));
        return cmsPostMapper.toDto(post);
    }

    @Transactional(readOnly = true)
    public List<CmsPostDto> getAllAdminPosts() {
        return cmsPostRepository.findAll().stream()
            .map(cmsPostMapper::toDto)
            .toList();
    }

    @Transactional
    public CmsPostDto createPost(UpsertCmsPostRequestDto dto) {
        if (cmsPostRepository.existsBySlug(dto.getSlug())) {
            throw new BusinessLogicException("CMS post slug already exists.");
        }

        CmsPost post = cmsPostMapper.toEntity(dto);
        applyPublicationState(post, dto.isPublished());
        return cmsPostMapper.toDto(cmsPostRepository.save(post));
    }

    @Transactional
    public CmsPostDto updatePost(UUID publicId, UpsertCmsPostRequestDto dto) {
        CmsPost post = cmsPostRepository.findByPublicId(publicId)
            .orElseThrow(() -> new ResourceNotFoundException("CMS post not found for publicId: " + publicId));

        if (cmsPostRepository.existsBySlugAndPublicIdNot(dto.getSlug(), publicId)) {
            throw new BusinessLogicException("CMS post slug already exists.");
        }

        cmsPostMapper.updateEntity(dto, post);
        applyPublicationState(post, dto.isPublished());
        return cmsPostMapper.toDto(cmsPostRepository.save(post));
    }

    @Transactional
    public void deletePost(UUID publicId) {
        CmsPost post = cmsPostRepository.findByPublicId(publicId)
            .orElseThrow(() -> new ResourceNotFoundException("CMS post not found for publicId: " + publicId));
        cmsPostRepository.delete(post);
    }

    private void applyPublicationState(CmsPost post, boolean published) {
        post.setPublished(published);
        if (published && post.getPublishedAt() == null) {
            post.setPublishedAt(LocalDateTime.now());
        }
        if (!published) {
            post.setPublishedAt(null);
        }
    }

}
