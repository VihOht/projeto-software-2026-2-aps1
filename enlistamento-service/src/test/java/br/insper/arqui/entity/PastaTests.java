package br.insper.arqui.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PastaTests {

    @Test
    void deveRetornarDescricao() {
        assertEquals("favorito", Pasta.FAVORITO.getDescricao());
        assertEquals("lista de compras", Pasta.LISTA_DE_COMPRAS.getDescricao());
        assertEquals("quero jogar", Pasta.QUERO_JOGAR.getDescricao());
    }

    @Test
    void deveConverterFavorito() {
        assertEquals(Pasta.FAVORITO, Pasta.fromValue("favorito"));
    }

    @Test
    void deveConverterListaDeComprasComUnderscore() {
        assertEquals(
                Pasta.LISTA_DE_COMPRAS,
                Pasta.fromValue("LISTA_DE_COMPRAS")
        );
    }

    @Test
    void deveConverterQueroJogarComHifen() {
        assertEquals(
                Pasta.QUERO_JOGAR,
                Pasta.fromValue("quero-jogar")
        );
    }

    @Test
    void deveIgnorarEspacosEMaiusculas() {
        assertEquals(
                Pasta.FAVORITO,
                Pasta.fromValue("  FAVORITO  ")
        );
    }

    @Test
    void deveRetornarNullQuandoValorForNull() {
        assertNull(Pasta.fromValue(null));
    }

    @Test
    void deveFalharQuandoPastaForDesconhecida() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Pasta.fromValue("pasta inexistente")
        );
    }
}