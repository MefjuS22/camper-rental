package com.camper.rental.service.cms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.camper.rental.dto.cms.CmsPostDto;
import com.camper.rental.dto.cms.UpsertCmsPostRequestDto;
import com.camper.rental.entity.cms.CmsPost;
import com.camper.rental.exception.BusinessLogicException;
import com.camper.rental.mapper.cms.CmsPostMapper;
import com.camper.rental.repository.cms.CmsPostRepository;

@ExtendWith(MockitoExtension.class)
class CmsPostServiceTest {

    @Mock
    private CmsPostRepository cmsPostRepository;
    @Mock
    private CmsPostMapper cmsPostMapper;

    private CmsPostService cmsPostService;

    @BeforeEach
    void setUp() {
        cmsPostService = new CmsPostService(cmsPostRepository, cmsPostMapper);
    }

    @Test
    void createPost_shouldSetPublishedAtWhenPublished() {
        UpsertCmsPostRequestDto dto = UpsertCmsPostRequestDto.builder()
            .title("Title")
            .slug("my-post")
            .content("Content")
            .published(true)
            .build();

        CmsPost entity = new CmsPost();
        CmsPost saved = new CmsPost();
        saved.setPublicId(UUID.randomUUID());

        CmsPostDto response = CmsPostDto.builder().title("Title").slug("my-post").published(true).build();

        when(cmsPostRepository.existsBySlug("my-post")).thenReturn(false);
        when(cmsPostMapper.toEntity(dto)).thenReturn(entity);
        when(cmsPostRepository.save(any(CmsPost.class))).thenReturn(saved);
        when(cmsPostMapper.toDto(saved)).thenReturn(response);

        CmsPostDto result = cmsPostService.createPost(dto);

        assertThat(result.isPublished()).isTrue();
        ArgumentCaptor<CmsPost> captor = ArgumentCaptor.forClass(CmsPost.class);
        verify(cmsPostRepository).save(captor.capture());
        assertThat(captor.getValue().isPublished()).isTrue();
        assertThat(captor.getValue().getPublishedAt()).isNotNull();
    }

    @Test
    void updatePost_shouldKeepPublishedAtWhenAlreadyPublished() {
        UUID publicId = UUID.randomUUID();
        UpsertCmsPostRequestDto dto = UpsertCmsPostRequestDto.builder()
            .title("Updated")
            .slug("updated-post")
            .content("Content")
            .published(true)
            .build();

        CmsPost existing = new CmsPost();
        LocalDateTime existingPublishedAt = LocalDateTime.of(2026, 1, 10, 10, 0);
        existing.setPublishedAt(existingPublishedAt);
        existing.setPublicId(publicId);

        CmsPostDto response = CmsPostDto.builder().title("Updated").published(true).build();

        when(cmsPostRepository.findByPublicId(publicId)).thenReturn(Optional.of(existing));
        when(cmsPostRepository.existsBySlugAndPublicIdNot("updated-post", publicId)).thenReturn(false);
        when(cmsPostRepository.save(existing)).thenReturn(existing);
        when(cmsPostMapper.toDto(existing)).thenReturn(response);

        CmsPostDto result = cmsPostService.updatePost(publicId, dto);

        assertThat(result.isPublished()).isTrue();
        assertThat(existing.getPublishedAt()).isEqualTo(existingPublishedAt);
        verify(cmsPostMapper).updateEntity(dto, existing);
    }

    @Test
    void createPost_shouldRejectDuplicateSlug() {
        UpsertCmsPostRequestDto dto = UpsertCmsPostRequestDto.builder()
            .title("Title")
            .slug("my-post")
            .content("Content")
            .build();

        when(cmsPostRepository.existsBySlug(eq("my-post"))).thenReturn(true);

        assertThatThrownBy(() -> cmsPostService.createPost(dto))
            .isInstanceOf(BusinessLogicException.class)
            .hasMessage("CMS post slug already exists.");
    }
}
