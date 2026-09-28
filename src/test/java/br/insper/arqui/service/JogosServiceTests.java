package br.insper.arqui.service;

import br.insper.arqui.dto.JogosDto;
import br.insper.arqui.entity.Jogos;
import br.insper.arqui.entity.Pasta;
import br.insper.arqui.exception.JogosNaoEncontradoException;
import br.insper.arqui.exception.ValidacaoJogosException;
import br.insper.arqui.repository.JogosRepository;
import br.insper.arqui.validator.ValidadorJogos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class JogosServiceTests {

	@InjectMocks
	private JogosService jogosService;

	@Mock
	private JogosRepository jogosRepository;

	@Mock
	private ValidadorJogos validadorJogos;

	@Test
	public void test_shouldCreateJogo() {
		JogosDto dto = criarDto("DOOM", Pasta.FAVORITO);
		Jogos salvo = criarJogo(1L, "DOOM", Pasta.FAVORITO);
		Mockito.when(jogosRepository.existsByTituloAndPlataforma("DOOM", "PC"))
				.thenReturn(false);
		Mockito.when(jogosRepository.save(Mockito.any(Jogos.class))).thenReturn(salvo);

		Jogos response = jogosService.criar(dto);

		Assertions.assertEquals(1L, response.getId());
		Assertions.assertEquals("DOOM", response.getTitulo());
		Assertions.assertEquals(Pasta.FAVORITO, response.getPasta());
		Mockito.verify(validadorJogos).validar(dto);
	}

	@Test
	public void test_shouldNotCreateDuplicateJogo() {
		JogosDto dto = criarDto("DOOM", Pasta.FAVORITO);
		Mockito.when(jogosRepository.existsByTituloAndPlataforma("DOOM", "PC"))
				.thenReturn(true);

		ValidacaoJogosException exception = Assertions.assertThrows(
				ValidacaoJogosException.class,
				() -> jogosService.criar(dto)
		);

		Assertions.assertEquals("O jogo já está cadastrado para essa plataforma", exception.getMessage());
		Mockito.verify(jogosRepository, Mockito.never()).save(Mockito.any());
	}

	@Test
	public void test_shouldReturnAllJogos() {
		List<Jogos> jogos = List.of(
				criarJogo(1L, "DOOM", Pasta.FAVORITO),
				criarJogo(2L, "Minecraft", Pasta.QUERO_JOGAR)
		);
		Mockito.when(jogosRepository.findAll()).thenReturn(jogos);

		List<Jogos> response = jogosService.listarTodos();

		Assertions.assertEquals(2, response.size());
	}

	@Test
	public void test_shouldReturnJogosByPasta() {
		List<Jogos> favoritos = List.of(criarJogo(1L, "DOOM", Pasta.FAVORITO));
		Mockito.when(jogosRepository.findAllByPasta(Pasta.FAVORITO)).thenReturn(favoritos);

		List<Jogos> response = jogosService.listarPorPasta(Pasta.FAVORITO);

		Assertions.assertEquals(1, response.size());
		Assertions.assertEquals(Pasta.FAVORITO, response.getFirst().getPasta());
	}

	@Test
	public void test_shouldReturnJogoById() {
		Jogos jogo = criarJogo(1L, "DOOM", Pasta.FAVORITO);
		Mockito.when(jogosRepository.findById(1L)).thenReturn(Optional.of(jogo));

		Jogos response = jogosService.obterPorId(1L);

		Assertions.assertEquals(1L, response.getId());
		Assertions.assertEquals("DOOM", response.getTitulo());
	}

	@Test
	public void test_shouldThrowWhenJogoDoesNotExist() {
		Mockito.when(jogosRepository.findById(99L)).thenReturn(Optional.empty());

		Assertions.assertThrows(
				JogosNaoEncontradoException.class,
				() -> jogosService.obterPorId(99L)
		);
	}

	@Test
	public void test_shouldUpdateJogo() {
		Jogos existente = criarJogo(1L, "DOOM", Pasta.QUERO_JOGAR);
		JogosDto dto = criarDto("DOOM Eternal", Pasta.FAVORITO);
		Mockito.when(jogosRepository.findById(1L)).thenReturn(Optional.of(existente));
		Mockito.when(jogosRepository.existsByTituloAndPlataforma("DOOM Eternal", "PC"))
				.thenReturn(false);
		Mockito.when(jogosRepository.save(existente)).thenReturn(existente);

		Jogos response = jogosService.atualizar(1L, dto);

		Assertions.assertEquals("DOOM Eternal", response.getTitulo());
		Assertions.assertEquals(Pasta.FAVORITO, response.getPasta());
		Mockito.verify(validadorJogos).validar(dto);
	}

	@Test
	public void test_shouldDeleteJogo() {
		Jogos jogo = criarJogo(1L, "DOOM", Pasta.FAVORITO);
		Mockito.when(jogosRepository.findById(1L)).thenReturn(Optional.of(jogo));

		jogosService.deletar(1L);

		Mockito.verify(jogosRepository).delete(jogo);
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
