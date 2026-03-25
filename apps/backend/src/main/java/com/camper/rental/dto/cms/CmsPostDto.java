package com.camper.rental.dto.cms;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmsPostDto {
    private UUID publicId;
    private String title;
    private String slug;
    private String excerpt;
    private String content;
    private String imageUrl;
    private boolean published;
    private LocalDateTime publishedAt;
}
