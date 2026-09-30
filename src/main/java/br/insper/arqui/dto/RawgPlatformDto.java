package br.insper.arqui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RawgPlatformDto(
        Long id,
        String name,
        String slug
) {
}