package br.insper.arqui.controller;

import br.insper.arqui.dto.JogosDto;
import br.insper.arqui.entity.Jogos;
import br.insper.arqui.entity.Pasta;
import br.insper.arqui.service.JogosService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class JogosControllerTests {

	@InjectMocks
	private JogosController jogosController;

	@Mock
	private JogosService jogosService;

	@Test
	public void test_shouldCreateJogo() {
		JogosDto dto = criarDto("DOOM", Pasta.FAVORITO);
		Jogos jogo = criarJogo(1L, "DOOM", Pasta.FAVORITO);
		Mockito.when(jogosService.criar(dto)).thenReturn(jogo);

		Jogos response = jogosController.criar(dto);

		Assertions.assertEquals(1L, response.getId());
		Assertions.assertEquals("DOOM", response.getTitulo());
		Assertions.assertEquals(Pasta.FAVORITO, response.getPasta());
	}

	@Test
	public void test_shouldReturnAllJogosWhenPastaIsNotProvided() {
		List<Jogos> jogos = List.of(
				criarJogo(1L, "DOOM", Pasta.FAVORITO),
				criarJogo(2L, "Minecraft", Pasta.QUERO_JOGAR)
		);
		Mockito.when(jogosService.listarTodos()).thenReturn(jogos);

		List<Jogos> response = jogosController.listar(null);

		Assertions.assertEquals(2, response.size());
		Mockito.verify(jogosService).listarTodos();
		Mockito.verify(jogosService, Mockito.never()).listarPorPasta(Mockito.any());
	}

	@Test
	public void test_shouldReturnJogosByPasta() {
		List<Jogos> favoritos = List.of(criarJogo(1L, "DOOM", Pasta.FAVORITO));
		Mockito.when(jogosService.listarPorPasta(Pasta.FAVORITO)).thenReturn(favoritos);

		List<Jogos> response = jogosController.listar(Pasta.FAVORITO);

		Assertions.assertEquals(1, response.size());
		Assertions.assertEquals(Pasta.FAVORITO, response.getFirst().getPasta());
		Mockito.verify(jogosService).listarPorPasta(Pasta.FAVORITO);
	}

	@Test
	public void test_shouldReturnJogoById() {
		Jogos jogo = criarJogo(1L, "DOOM", Pasta.FAVORITO);
		Mockito.when(jogosService.obterPorId(1L)).thenReturn(jogo);

		Jogos response = jogosController.obter(1L);

		Assertions.assertEquals(1L, response.getId());
		Assertions.assertEquals("DOOM", response.getTitulo());
	}

	@Test
	public void test_shouldUpdateJogo() {
		JogosDto dto = criarDto("DOOM Eternal", Pasta.FAVORITO);
		Jogos atualizado = criarJogo(1L, "DOOM Eternal", Pasta.FAVORITO);
		Mockito.when(jogosService.atualizar(1L, dto)).thenReturn(atualizado);

		Jogos response = jogosController.atualizar(1L, dto);

		Assertions.assertEquals("DOOM Eternal", response.getTitulo());
		Mockito.verify(jogosService).atualizar(1L, dto);
	}

	@Test
	public void test_shouldDeleteJogo() {
		jogosController.deletar(1L);

		Mockito.verify(jogosService).deletar(1L);
	}

	private JogosDto criarDto(String titulo, Pasta pasta) {
		return new JogosDto(
				titulo,
				LocalDate.of(2016, 5, 13),
				12,
				"FPS",
				pasta,
				"DOOM Original Game Soundtrack",
				"id Software",
				"PC"
		);
	}

	private Jogos criarJogo(Long id, String titulo, Pasta pasta) {
		return new Jogos(
				id,
				titulo,
				LocalDate.of(2016, 5, 13),
				12,
				"FPS",
				pasta,
				"DOOM Original Game Soundtrack",
				"id Software",
				"PC"
		);
	}
}
