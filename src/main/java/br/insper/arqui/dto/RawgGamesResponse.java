package br.insper.arqui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RawgGamesResponse(
        Integer count,
        List<RawgGameDto> results
) {
}