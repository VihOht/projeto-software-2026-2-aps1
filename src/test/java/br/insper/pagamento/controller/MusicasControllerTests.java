package br.insper.pagamento.controller;

import br.insper.pagamento.dto.MusicasDto;
import br.insper.pagamento.entity.Musicas;
import br.insper.pagamento.service.MusicasService;
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
public class MusicasControllerTests {

	@InjectMocks
	private MusicasController musicasController;

	@Mock
	private MusicasService musicasService;

	@Test
	public void test_shouldCreateMusica() {
		MusicasDto dto = criarDto("BFG Division");
		Musicas musica = criarMusica(1L, "BFG Division");
		Mockito.when(musicasService.criar(dto)).thenReturn(musica);

		Musicas response = musicasController.criar(dto);

		Assertions.assertEquals(1L, response.getId());
		Assertions.assertEquals("BFG Division", response.getTitulo());
	}

	@Test
	public void test_shouldReturnAllMusicasWhenJogoIdIsNotProvided() {
		List<Musicas> musicas = List.of(
				criarMusica(1L, "BFG Division"),
				criarMusica(2L, "Rip & Tear")
		);
		Mockito.when(musicasService.listarTodos()).thenReturn(musicas);

		List<Musicas> response = musicasController.listar(null);

		Assertions.assertEquals(2, response.size());
		Mockito.verify(musicasService).listarTodos();
		Mockito.verify(musicasService, Mockito.never()).listarPorJogo(Mockito.any());
	}

	@Test
	public void test_shouldReturnMusicasByJogo() {
		List<Musicas> musicas = List.of(criarMusica(1L, "BFG Division"));
		Mockito.when(musicasService.listarPorJogo(10L)).thenReturn(musicas);

		List<Musicas> response = musicasController.listar(10L);

		Assertions.assertEquals(1, response.size());
		Assertions.assertEquals(10L, response.getFirst().getJogoId());
		Mockito.verify(musicasService).listarPorJogo(10L);
	}

	@Test
	public void test_shouldReturnMusicaById() {
		Musicas musica = criarMusica(1L, "BFG Division");
		Mockito.when(musicasService.obterPorId(1L)).thenReturn(musica);

		Musicas response = musicasController.obter(1L);

		Assertions.assertEquals(1L, response.getId());
		Assertions.assertEquals("BFG Division", response.getTitulo());
	}

	@Test
	public void test_shouldUpdateMusica() {
		MusicasDto dto = criarDto("Rip & Tear");
		Musicas atualizada = criarMusica(1L, "Rip & Tear");
		Mockito.when(musicasService.atualizar(1L, dto)).thenReturn(atualizada);

		Musicas response = musicasController.atualizar(1L, dto);

		Assertions.assertEquals("Rip & Tear", response.getTitulo());
		Mockito.verify(musicasService).atualizar(1L, dto);
	}

	@Test
	public void test_shouldDeleteMusica() {
		musicasController.deletar(1L);

		Mockito.verify(musicasService).deletar(1L);
	}

	private MusicasDto criarDto(String titulo) {
		return new MusicasDto(
				10L,
				titulo,
				"Mick Gordon",
				"DOOM Original Game Soundtrack",
				LocalDate.of(2016, 5, 13),
				225,
				"Metal"
		);
	}

	private Musicas criarMusica(Long id, String titulo) {
		return new Musicas(
				id,
				10L,
				titulo,
				"Mick Gordon",
				"DOOM Original Game Soundtrack",
				LocalDate.of(2016, 5, 13),
				225,
				"Metal"
		);
	}
}
