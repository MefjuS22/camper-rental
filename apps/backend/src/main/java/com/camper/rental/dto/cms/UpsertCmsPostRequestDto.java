package com.camper.rental.dto.cms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpsertCmsPostRequestDto {
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title max length is 200")
    private String title;

    @NotBlank(message = "Slug is required")
    @Size(max = 200, message = "Slug max length is 200")
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Slug must be lowercase kebab-case")
    private String slug;

    @Size(max = 1000, message = "Excerpt max length is 1000")
    private String excerpt;

    @NotBlank(message = "Content is required")
    private String content;

    @Size(max = 1000, message = "Image URL max length is 1000")
    private String imageUrl;
    private boolean published;
}
