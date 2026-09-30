package br.insper.arqui.validator;

import br.insper.arqui.dto.EnlistamentoDto;
import br.insper.arqui.entity.Pasta;
import br.insper.arqui.exception.ValidacaoEnlistamentoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidadorEnlistamentoTests {

    private ValidadorEnlistamento validador;

    @BeforeEach
    void setup() {
        validador = new ValidadorEnlistamento();
    }

    private EnlistamentoDto dtoValido() {
        EnlistamentoDto dto = new EnlistamentoDto();
        dto.setJogoId(1L);
        dto.setPasta(Pasta.FAVORITO);
        return dto;
    }

    @Test
    void deveAceitarDtoValido() {
        assertDoesNotThrow(() -> validador.validar(dtoValido()));
    }

    @Test
    void deveFalharQuandoDtoForNulo() {
        ValidacaoEnlistamentoException ex = assertThrows(
                ValidacaoEnlistamentoException.class,
                () -> validador.validar(null)
        );

        assertEquals("Os dados do enlistamento são obrigatórios", ex.getMessage());
    }

    @Test
    void deveFalharQuandoJogoIdForNulo() {
        EnlistamentoDto dto = dtoValido();
        dto.setJogoId(null);

        assertThrows(
                ValidacaoEnlistamentoException.class,
                () -> validador.validar(dto)
        );
    }

    @Test
    void deveFalharQuandoJogoIdForZero() {
        EnlistamentoDto dto = dtoValido();
        dto.setJogoId(0L);

        assertThrows(
                ValidacaoEnlistamentoException.class,
                () -> validador.validar(dto)
        );
    }

    @Test
    void deveFalharQuandoPastaForNula() {
        EnlistamentoDto dto = dtoValido();
        dto.setPasta(null);

        assertThrows(
                ValidacaoEnlistamentoException.class,
                () -> validador.validar(dto)
        );
    }
}