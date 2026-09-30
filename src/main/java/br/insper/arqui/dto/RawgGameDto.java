package br.insper.arqui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RawgGameDto(
        Long id,
        String name,
        LocalDate released,
        Double rating,
        String background_image,
        List<RawgGenreDto> genres,
        List<RawgPlatformWrapperDto> platforms
) {
}