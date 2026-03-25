package com.camper.rental.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class OpenApiResponseRequiredPropertiesConfig {
    @Bean
    public OpenApiCustomizer forceRequiredForResponseDtos() {
        return this::customise;
    }

    private void customise(OpenAPI openApi) {
        if (openApi == null || openApi.getComponents() == null || openApi.getComponents().getSchemas() == null) return;

        Set<String> responseComponentSchemaNames = new HashSet<>();
        collectResponseSchemasRefs(openApi, responseComponentSchemaNames);

        responseComponentSchemaNames.forEach(schemaName -> {
            Schema<?> schema = openApi.getComponents().getSchemas().get(schemaName);
            if (schema == null || schema.getProperties() == null) return;

            List<String> required = schema.getRequired();
            if (required == null) required = new ArrayList<>();

            for (String propName : schema.getProperties().keySet()) {
                if (!required.contains(propName)) required.add(propName);
            }

            schema.setRequired(required);
        });
    }

    private void collectResponseSchemasRefs(OpenAPI openApi, Set<String> out) {
        if (openApi.getPaths() == null) return;

        for (Map.Entry<String, PathItem> entry : openApi.getPaths().entrySet()) {
            PathItem pathItem = entry.getValue();
            if (pathItem == null) continue;

            for (Operation operation : pathItem.readOperations()) {
                if (operation == null) continue;
                if (operation.getResponses() == null) continue;

                for (Map.Entry<String, ApiResponse> respEntry : operation.getResponses().entrySet()) {
                    ApiResponse apiResponse = respEntry.getValue();
                    if (apiResponse == null || apiResponse.getContent() == null) continue;

                    for (Map.Entry<String, MediaType> mediaEntry : apiResponse.getContent().entrySet()) {
                        MediaType mediaType = mediaEntry.getValue();
                        if (mediaType == null) continue;
                        collectComponentRefsFromSchema(mediaType.getSchema(), out);
                    }
                }
            }
        }
    }

    private void collectComponentRefsFromSchema(Schema<?> schema, Set<String> out) {
        if (schema == null) return;

        if (schema.get$ref() != null) {
            String ref = schema.get$ref();
            String prefix = "#/components/schemas/";
            if (ref.startsWith(prefix)) out.add(ref.substring(prefix.length()));
            return;
        }

        if (schema.getProperties() != null) {
            for (Schema<?> propSchema : schema.getProperties().values()) {
                collectComponentRefsFromSchema(propSchema, out);
            }
        }

        if (schema.getItems() != null) {
            collectComponentRefsFromSchema(schema.getItems(), out);
        }

        if (schema.getAllOf() != null) {
            for (Schema<?> s : schema.getAllOf()) collectComponentRefsFromSchema(s, out);
        }
        if (schema.getAnyOf() != null) {
            for (Schema<?> s : schema.getAnyOf()) collectComponentRefsFromSchema(s, out);
        }
        if (schema.getOneOf() != null) {
            for (Schema<?> s : schema.getOneOf()) collectComponentRefsFromSchema(s, out);
        }
    }
}

