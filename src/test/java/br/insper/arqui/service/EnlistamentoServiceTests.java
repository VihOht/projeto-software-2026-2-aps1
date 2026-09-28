package br.insper.arqui.service;

import br.insper.arqui.dto.EnlistamentoDto;
import br.insper.arqui.entity.Enlistamento;
import br.insper.arqui.entity.Pasta;
import br.insper.arqui.exception.EnlistamentoNaoEncontradoException;
import br.insper.arqui.exception.ValidacaoEnlistamentoException;
import br.insper.arqui.repository.EnlistamentoRepository;
import br.insper.arqui.validator.ValidadorEnlistamento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EnlistamentoServiceTests {

	@InjectMocks
	private EnlistamentoService enlistamentoService;

	@Mock
	private EnlistamentoRepository enlistamentoRepository;

	@Mock
	private ValidadorEnlistamento validadorEnlistamento;

	@Test
	public void test_shouldCreateEnlistamento() {
		EnlistamentoDto dto = criarDto(10L, Pasta.FAVORITO);
		Enlistamento salvo = criarEnlistamento(1L, 10L, Pasta.FAVORITO);

		Mockito.when(enlistamentoRepository.existsByJogoIdAndPasta(10L, Pasta.FAVORITO))
				.thenReturn(false);
		Mockito.when(enlistamentoRepository.save(Mockito.any(Enlistamento.class)))
				.thenReturn(salvo);

		Enlistamento response = enlistamentoService.criar(dto);

		Assertions.assertEquals(1L, response.getId());
		Assertions.assertEquals(10L, response.getJogoId());
		Assertions.assertEquals(Pasta.FAVORITO, response.getPasta());
		Mockito.verify(validadorEnlistamento).validar(dto);
	}

	@Test
	public void test_shouldNotCreateDuplicateEnlistamento() {
		EnlistamentoDto dto = criarDto(10L, Pasta.FAVORITO);
		Mockito.when(enlistamentoRepository.existsByJogoIdAndPasta(10L, Pasta.FAVORITO))
				.thenReturn(true);

		ValidacaoEnlistamentoException exception = Assertions.assertThrows(
				ValidacaoEnlistamentoException.class,
				() -> enlistamentoService.criar(dto)
		);

		Assertions.assertEquals("O jogo já está nessa pasta", exception.getMessage());
		Mockito.verify(enlistamentoRepository, Mockito.never()).save(Mockito.any());
	}

	@Test
	public void test_shouldReturnAllEnlistamentos() {
		List<Enlistamento> enlistamentos = List.of(
				criarEnlistamento(1L, 10L, Pasta.FAVORITO),
				criarEnlistamento(2L, 20L, Pasta.QUERO_JOGAR)
		);
		Mockito.when(enlistamentoRepository.findAll()).thenReturn(enlistamentos);

		List<Enlistamento> response = enlistamentoService.listarTodos();

		Assertions.assertEquals(2, response.size());
	}

	@Test
	public void test_shouldReturnEnlistamentosByPasta() {
		List<Enlistamento> favoritos = List.of(
				criarEnlistamento(1L, 10L, Pasta.FAVORITO)
		);
		Mockito.when(enlistamentoRepository.findAllByPasta(Pasta.FAVORITO))
				.thenReturn(favoritos);

		List<Enlistamento> response = enlistamentoService.listarPorPasta(Pasta.FAVORITO);

		Assertions.assertEquals(1, response.size());
		Assertions.assertEquals(Pasta.FAVORITO, response.getFirst().getPasta());
	}

	@Test
	public void test_shouldReturnEnlistamentoById() {
		Enlistamento enlistamento = criarEnlistamento(1L, 10L, Pasta.LISTA_DE_COMPRAS);
		Mockito.when(enlistamentoRepository.findById(1L)).thenReturn(Optional.of(enlistamento));

		Enlistamento response = enlistamentoService.obterPorId(1L);

		Assertions.assertEquals(1L, response.getId());
		Assertions.assertEquals(Pasta.LISTA_DE_COMPRAS, response.getPasta());
	}

	@Test
	public void test_shouldThrowWhenEnlistamentoDoesNotExist() {
		Mockito.when(enlistamentoRepository.findById(99L)).thenReturn(Optional.empty());

		Assertions.assertThrows(
				EnlistamentoNaoEncontradoException.class,
				() -> enlistamentoService.obterPorId(99L)
		);
	}

	@Test
	public void test_shouldUpdateEnlistamento() {
		Enlistamento existente = criarEnlistamento(1L, 10L, Pasta.QUERO_JOGAR);
		EnlistamentoDto dto = criarDto(10L, Pasta.FAVORITO);

		Mockito.when(enlistamentoRepository.findById(1L)).thenReturn(Optional.of(existente));
		Mockito.when(enlistamentoRepository.existsByJogoIdAndPasta(10L, Pasta.FAVORITO))
				.thenReturn(false);
		Mockito.when(enlistamentoRepository.save(existente)).thenReturn(existente);

		Enlistamento response = enlistamentoService.atualizar(1L, dto);

		Assertions.assertEquals(Pasta.FAVORITO, response.getPasta());
		Mockito.verify(validadorEnlistamento).validar(dto);
	}

	@Test
	public void test_shouldDeleteEnlistamento() {
		Enlistamento enlistamento = criarEnlistamento(1L, 10L, Pasta.FAVORITO);
		Mockito.when(enlistamentoRepository.findById(1L)).thenReturn(Optional.of(enlistamento));

		enlistamentoService.deletar(1L);

		Mockito.verify(enlistamentoRepository).delete(enlistamento);
	}

	private EnlistamentoDto criarDto(Long jogoId, Pasta pasta) {
		return new EnlistamentoDto(jogoId, pasta);
	}

	private Enlistamento criarEnlistamento(Long id, Long jogoId, Pasta pasta) {
		return new Enlistamento(id, jogoId, pasta);
	}
}
