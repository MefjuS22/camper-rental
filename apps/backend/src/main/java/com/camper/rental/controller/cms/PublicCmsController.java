package com.camper.rental.controller.cms;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.camper.rental.dto.cms.CmsPostDto;
import com.camper.rental.service.cms.CmsPostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/public/cms/posts")
@RequiredArgsConstructor
@Tag(name = "Public CMS")
public class PublicCmsController {

    private final CmsPostService cmsPostService;

    @GetMapping
    @Operation(summary = "List published posts", description = "Returns all published CMS posts for web/mobile clients.")
    public ResponseEntity<List<CmsPostDto>> listPublished() {
        return ResponseEntity.ok(cmsPostService.getPublishedPosts());
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Get published post by slug", description = "Returns single published CMS post by slug.")
    public ResponseEntity<CmsPostDto> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(cmsPostService.getPublishedBySlug(slug));
    }
}
