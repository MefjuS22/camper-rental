package com.camper.rental.controller.cms;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.camper.rental.dto.cms.CmsPostDto;
import com.camper.rental.dto.cms.UpsertCmsPostRequestDto;
import com.camper.rental.service.cms.CmsPostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/cms/posts")
@RequiredArgsConstructor
@Tag(name = "Admin CMS")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCmsController {

    private final CmsPostService cmsPostService;

    @GetMapping
    @Operation(summary = "List all CMS posts", description = "Returns all CMS posts including unpublished.")
    public ResponseEntity<List<CmsPostDto>> listAll() {
        return ResponseEntity.ok(cmsPostService.getAllAdminPosts());
    }

    @PostMapping
    @Operation(summary = "Create CMS post", description = "Creates new CMS post.")
    public ResponseEntity<CmsPostDto> create(@Valid @RequestBody UpsertCmsPostRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cmsPostService.createPost(requestDto));
    }

    @PutMapping("/{publicId}")
    @Operation(summary = "Update CMS post", description = "Updates existing CMS post.")
    public ResponseEntity<CmsPostDto> update(
        @PathVariable UUID publicId,
        @Valid @RequestBody UpsertCmsPostRequestDto requestDto
    ) {
        return ResponseEntity.ok(cmsPostService.updatePost(publicId, requestDto));
    }

    @DeleteMapping("/{publicId}")
    @Operation(summary = "Delete CMS post", description = "Deletes CMS post by publicId.")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        cmsPostService.deletePost(publicId);
        return ResponseEntity.noContent().build();
    }
}
