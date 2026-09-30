package br.insper.arqui.validator;

import br.insper.arqui.dto.MusicasDto;
import br.insper.arqui.exception.ValidacaoMusicasException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidadorMusicasTests {

    private ValidadorMusicas validador;

    @BeforeEach
    void setup() {
        validador = new ValidadorMusicas();
    }

    private MusicasDto dtoValido() {
        MusicasDto dto = new MusicasDto();
        dto.setJogoId(1L);
        dto.setTitulo("Sweden");
        dto.setArtista("C418");
        dto.setDataLancamento(LocalDate.of(2011, 11, 18));
        dto.setDuracaoSegundos(215);
        return dto;
    }

    @Test
    void deveAceitarDtoValido() {
        assertDoesNotThrow(() -> validador.validar(dtoValido()));
    }

    @Test
    void deveFalharQuandoDtoForNulo() {
        ValidacaoMusicasException ex = assertThrows(
                ValidacaoMusicasException.class,
                () -> validador.validar(null)
        );

        assertEquals("Os dados da música são obrigatórios", ex.getMessage());
    }

    @Test
    void deveFalharQuandoJogoIdForNulo() {
        MusicasDto dto = dtoValido();
        dto.setJogoId(null);

        assertThrows(
                ValidacaoMusicasException.class,
                () -> validador.validar(dto)
        );
    }

    @Test
    void deveFalharQuandoJogoIdForZero() {
        MusicasDto dto = dtoValido();
        dto.setJogoId(0L);

        assertThrows(
                ValidacaoMusicasException.class,
                () -> validador.validar(dto)
        );
    }

    @Test
    void deveFalharQuandoTituloForNulo() {
        MusicasDto dto = dtoValido();
        dto.setTitulo(null);

        assertThrows(
                ValidacaoMusicasException.class,
                () -> validador.validar(dto)
        );
    }

    @Test
    void deveFalharQuandoArtistaForNulo() {
        MusicasDto dto = dtoValido();
        dto.setArtista(null);

        assertThrows(
                ValidacaoMusicasException.class,
                () -> validador.validar(dto)
        );
    }

    @Test
    void deveFalharQuandoDataLancamentoForNula() {
        MusicasDto dto = dtoValido();
        dto.setDataLancamento(null);

        assertThrows(
                ValidacaoMusicasException.class,
                () -> validador.validar(dto)
        );
    }

    @Test
    void deveFalharQuandoDuracaoForNula() {
        MusicasDto dto = dtoValido();
        dto.setDuracaoSegundos(null);

        assertThrows(
                ValidacaoMusicasException.class,
                () -> validador.validar(dto)
        );
    }

    @Test
    void deveFalharQuandoDuracaoForZero() {
        MusicasDto dto = dtoValido();
        dto.setDuracaoSegundos(0);

        assertThrows(
                ValidacaoMusicasException.class,
                () -> validador.validar(dto)
        );
    }
}