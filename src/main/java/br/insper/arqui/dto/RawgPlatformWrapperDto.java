package br.insper.arqui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RawgPlatformWrapperDto(
        RawgPlatformDto platform
) {
}