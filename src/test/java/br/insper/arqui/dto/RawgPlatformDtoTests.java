package br.insper.arqui.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RawgPlatformDtoTests {

    @Test
    void deveCriarPlataformaRawg() {
        RawgPlatformDto plataforma =
                new RawgPlatformDto(4L, "PC", "pc");

        assertEquals(4L, plataforma.id());
        assertEquals("PC", plataforma.name());
        assertEquals("pc", plataforma.slug());
    }
}