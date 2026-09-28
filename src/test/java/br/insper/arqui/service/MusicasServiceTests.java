package br.insper.arqui.service;

import br.insper.arqui.dto.MusicasDto;
import br.insper.arqui.entity.Musicas;
import br.insper.arqui.exception.MusicasNaoEncontradaException;
import br.insper.arqui.exception.ValidacaoMusicasException;
import br.insper.arqui.repository.MusicasRepository;
import br.insper.arqui.validator.ValidadorMusicas;
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
public class MusicasServiceTests {

	@InjectMocks
	private MusicasService musicasService;

	@Mock
	private MusicasRepository musicasRepository;

	@Mock
	private ValidadorMusicas validadorMusicas;

	@Test
	public void test_shouldCreateMusica() {
		MusicasDto dto = criarDto(10L, "BFG Division", "Mick Gordon");
		Musicas salva = criarMusica(1L, 10L, "BFG Division", "Mick Gordon");
		Mockito.when(musicasRepository.existsByJogoIdAndTituloAndArtista(
				10L, "BFG Division", "Mick Gordon")).thenReturn(false);
		Mockito.when(musicasRepository.save(Mockito.any(Musicas.class))).thenReturn(salva);

		Musicas response = musicasService.criar(dto);

		Assertions.assertEquals(1L, response.getId());
		Assertions.assertEquals("BFG Division", response.getTitulo());
		Assertions.assertEquals("Mick Gordon", response.getArtista());
		Mockito.verify(validadorMusicas).validar(dto);
	}

	@Test
	public void test_shouldNotCreateDuplicateMusica() {
		MusicasDto dto = criarDto(10L, "BFG Division", "Mick Gordon");
		Mockito.when(musicasRepository.existsByJogoIdAndTituloAndArtista(
				10L, "BFG Division", "Mick Gordon")).thenReturn(true);

		ValidacaoMusicasException exception = Assertions.assertThrows(
				ValidacaoMusicasException.class,
				() -> musicasService.criar(dto)
		);

		Assertions.assertEquals("A música já está cadastrada para esse jogo", exception.getMessage());
		Mockito.verify(musicasRepository, Mockito.never()).save(Mockito.any());
	}

	@Test
	public void test_shouldReturnAllMusicas() {
		List<Musicas> musicas = List.of(
				criarMusica(1L, 10L, "BFG Division", "Mick Gordon"),
				criarMusica(2L, 20L, "Sweden", "C418")
		);
		Mockito.when(musicasRepository.findAll()).thenReturn(musicas);

		List<Musicas> response = musicasService.listarTodos();

		Assertions.assertEquals(2, response.size());
	}

	@Test
	public void test_shouldReturnMusicasByJogo() {
		List<Musicas> musicas = List.of(
				criarMusica(1L, 10L, "BFG Division", "Mick Gordon")
		);
		Mockito.when(musicasRepository.findAllByJogoId(10L)).thenReturn(musicas);

		List<Musicas> response = musicasService.listarPorJogo(10L);

		Assertions.assertEquals(1, response.size());
		Assertions.assertEquals(10L, response.getFirst().getJogoId());
	}

	@Test
	public void test_shouldReturnMusicaById() {
		Musicas musica = criarMusica(1L, 10L, "BFG Division", "Mick Gordon");
		Mockito.when(musicasRepository.findById(1L)).thenReturn(Optional.of(musica));

		Musicas response = musicasService.obterPorId(1L);

		Assertions.assertEquals(1L, response.getId());
		Assertions.assertEquals("BFG Division", response.getTitulo());
	}

	@Test
	public void test_shouldThrowWhenMusicaDoesNotExist() {
		Mockito.when(musicasRepository.findById(99L)).thenReturn(Optional.empty());

		Assertions.assertThrows(
				MusicasNaoEncontradaException.class,
				() -> musicasService.obterPorId(99L)
		);
	}

	@Test
	public void test_shouldUpdateMusica() {
		Musicas existente = criarMusica(1L, 10L, "BFG Division", "Mick Gordon");
		MusicasDto dto = criarDto(10L, "Rip & Tear", "Mick Gordon");
		Mockito.when(musicasRepository.findById(1L)).thenReturn(Optional.of(existente));
		Mockito.when(musicasRepository.existsByJogoIdAndTituloAndArtista(
				10L, "Rip & Tear", "Mick Gordon")).thenReturn(false);
		Mockito.when(musicasRepository.save(existente)).thenReturn(existente);

		Musicas response = musicasService.atualizar(1L, dto);

		Assertions.assertEquals("Rip & Tear", response.getTitulo());
		Mockito.verify(validadorMusicas).validar(dto);
	}

	@Test
	public void test_shouldDeleteMusica() {
		Musicas musica = criarMusica(1L, 10L, "BFG Division", "Mick Gordon");
		Mockito.when(musicasRepository.findById(1L)).thenReturn(Optional.of(musica));

		musicasService.deletar(1L);

		Mockito.verify(musicasRepository).delete(musica);
	}

	private MusicasDto criarDto(Long jogoId, String titulo, String artista) {
		return new MusicasDto(
				jogoId,
				titulo,
				artista,
				"Original Game Soundtrack",
				LocalDate.of(2016, 5, 13),
				225,
				"Trilha sonora"
		);
	}

	private Musicas criarMusica(Long id, Long jogoId, String titulo, String artista) {
		return new Musicas(
				id,
				jogoId,
				titulo,
				artista,
				"Original Game Soundtrack",
				LocalDate.of(2016, 5, 13),
				225,
				"Trilha sonora"
		);
	}
}
