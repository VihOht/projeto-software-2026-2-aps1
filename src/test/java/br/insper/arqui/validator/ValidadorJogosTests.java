package br.insper.arqui.validator;

import br.insper.arqui.dto.JogosDto;
import br.insper.arqui.entity.Pasta;
import br.insper.arqui.exception.ValidacaoJogosException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidadorJogosTests {

    private ValidadorJogos validador;

    @BeforeEach
    void setup() {
        validador = new ValidadorJogos();
    }

    private JogosDto dtoValido() {
        JogosDto dto = new JogosDto();
        dto.setTitulo("Minecraft");
        dto.setDataLancamento(LocalDate.of(2011, 11, 18));
        dto.setDuracaoHoras(100);
        dto.setTipoJogo("Sandbox");
        dto.setPasta(Pasta.FAVORITO);
        dto.setTrilhaSonora("Original Soundtrack");
        dto.setDesenvolvedora("Mojang");
        dto.setPlataforma("PC");
        return dto;
    }

    @Test
    void deveAceitarDtoValido() {
        assertDoesNotThrow(() -> validador.validar(dtoValido()));
    }

    @Test
    void deveFalharQuandoDtoForNulo() {
        ValidacaoJogosException ex = assertThrows(
                ValidacaoJogosException.class,
                () -> validador.validar(null)
        );

        assertEquals("Os dados do jogo são obrigatórios", ex.getMessage());
    }

    @Test
    void deveFalharQuandoTituloForNulo() {
        JogosDto dto = dtoValido();
        dto.setTitulo(null);

        ValidacaoJogosException ex = assertThrows(
                ValidacaoJogosException.class,
                () -> validador.validar(dto)
        );

        assertEquals("Título é obrigatório", ex.getMessage());
    }

    @Test
    void deveFalharQuandoTituloForVazio() {
        JogosDto dto = dtoValido();
        dto.setTitulo(" ");

        assertThrows(
                ValidacaoJogosException.class,
                () -> validador.validar(dto)
        );
    }

    @Test
    void deveFalharQuandoDataLancamentoForNula() {
        JogosDto dto = dtoValido();
        dto.setDataLancamento(null);

        assertThrows(
                ValidacaoJogosException.class,
                () -> validador.validar(dto)
        );
    }

    @Test
    void devePermitirDuracaoNula() {
        JogosDto dto = dtoValido();
        dto.setDuracaoHoras(null);

        assertDoesNotThrow(() -> validador.validar(dto));
    }

    @Test
    void devePermitirDuracaoZero() {
        JogosDto dto = dtoValido();
        dto.setDuracaoHoras(0);

        assertDoesNotThrow(() -> validador.validar(dto));
    }

    @Test
    void deveFalharQuandoDuracaoForNegativa() {
        JogosDto dto = dtoValido();
        dto.setDuracaoHoras(-1);

        assertThrows(
                ValidacaoJogosException.class,
                () -> validador.validar(dto)
        );
    }

    @Test
    void deveFalharQuandoTipoJogoForNulo() {
        JogosDto dto = dtoValido();
        dto.setTipoJogo(null);

        assertThrows(
                ValidacaoJogosException.class,
                () -> validador.validar(dto)
        );
    }

    @Test
    void deveFalharQuandoPastaForNula() {
        JogosDto dto = dtoValido();
        dto.setPasta(null);

        assertThrows(
                ValidacaoJogosException.class,
                () -> validador.validar(dto)
        );
    }

    @Test
    void devePermitirTrilhaSonoraNula() {
        JogosDto dto = dtoValido();
        dto.setTrilhaSonora(null);

        assertDoesNotThrow(() -> validador.validar(dto));
    }

    @Test
    void devePermitirDesenvolvedoraNula() {
        JogosDto dto = dtoValido();
        dto.setDesenvolvedora(null);

        assertDoesNotThrow(() -> validador.validar(dto));
    }

    @Test
    void deveFalharQuandoPlataformaForNula() {
        JogosDto dto = dtoValido();
        dto.setPlataforma(null);

        assertThrows(
                ValidacaoJogosException.class,
                () -> validador.validar(dto)
        );
    }
}