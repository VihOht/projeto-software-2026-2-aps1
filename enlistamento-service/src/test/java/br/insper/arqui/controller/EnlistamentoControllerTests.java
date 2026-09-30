package br.insper.arqui.controller;

import br.insper.arqui.dto.EnlistamentoDto;
import br.insper.arqui.entity.Enlistamento;
import br.insper.arqui.entity.Pasta;
import br.insper.arqui.service.EnlistamentoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EnlistamentoControllerTests {

	@InjectMocks
	private EnlistamentoController enlistamentoController;

	@Mock
	private EnlistamentoService enlistamentoService;

	@Test
	public void test_shouldCreateEnlistamento() {
		EnlistamentoDto dto = new EnlistamentoDto(10L, Pasta.FAVORITO);
		Enlistamento enlistamento = new Enlistamento(1L, 10L, Pasta.FAVORITO);
		Mockito.when(enlistamentoService.criar(dto)).thenReturn(enlistamento);

		Enlistamento response = enlistamentoController.criar(dto);

		Assertions.assertEquals(1L, response.getId());
		Assertions.assertEquals(10L, response.getJogoId());
		Assertions.assertEquals(Pasta.FAVORITO, response.getPasta());
	}

	@Test
	public void test_shouldReturnAllEnlistamentosWhenPastaIsNotProvided() {
		List<Enlistamento> enlistamentos = List.of(
				new Enlistamento(1L, 10L, Pasta.FAVORITO),
				new Enlistamento(2L, 20L, Pasta.QUERO_JOGAR)
		);
		Mockito.when(enlistamentoService.listarTodos()).thenReturn(enlistamentos);

		List<Enlistamento> response = enlistamentoController.listar(null);

		Assertions.assertEquals(2, response.size());
		Mockito.verify(enlistamentoService).listarTodos();
		Mockito.verify(enlistamentoService, Mockito.never()).listarPorPasta(Mockito.any());
	}

	@Test
	public void test_shouldReturnEnlistamentosByPasta() {
		List<Enlistamento> favoritos = List.of(
				new Enlistamento(1L, 10L, Pasta.FAVORITO)
		);
		Mockito.when(enlistamentoService.listarPorPasta(Pasta.FAVORITO)).thenReturn(favoritos);

		List<Enlistamento> response = enlistamentoController.listar(Pasta.FAVORITO);

		Assertions.assertEquals(1, response.size());
		Assertions.assertEquals(Pasta.FAVORITO, response.getFirst().getPasta());
		Mockito.verify(enlistamentoService).listarPorPasta(Pasta.FAVORITO);
	}

	@Test
	public void test_shouldReturnEnlistamentoById() {
		Enlistamento enlistamento = new Enlistamento(1L, 10L, Pasta.LISTA_DE_COMPRAS);
		Mockito.when(enlistamentoService.obterPorId(1L)).thenReturn(enlistamento);

		Enlistamento response = enlistamentoController.obter(1L);

		Assertions.assertEquals(1L, response.getId());
		Assertions.assertEquals(Pasta.LISTA_DE_COMPRAS, response.getPasta());
	}

	@Test
	public void test_shouldUpdateEnlistamento() {
		EnlistamentoDto dto = new EnlistamentoDto(10L, Pasta.QUERO_JOGAR);
		Enlistamento atualizado = new Enlistamento(1L, 10L, Pasta.QUERO_JOGAR);
		Mockito.when(enlistamentoService.atualizar(1L, dto)).thenReturn(atualizado);

		Enlistamento response = enlistamentoController.atualizar(1L, dto);

		Assertions.assertEquals(Pasta.QUERO_JOGAR, response.getPasta());
		Mockito.verify(enlistamentoService).atualizar(1L, dto);
	}

	@Test
	public void test_shouldDeleteEnlistamento() {
		enlistamentoController.deletar(1L);

		Mockito.verify(enlistamentoService).deletar(1L);
	}
}
