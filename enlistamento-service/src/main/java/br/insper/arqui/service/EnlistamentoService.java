package br.insper.arqui.service;

import br.insper.arqui.client.JogosClient;
import br.insper.arqui.dto.EnlistamentoDto;
import br.insper.arqui.entity.Enlistamento;
import br.insper.arqui.entity.Pasta;
import br.insper.arqui.exception.EnlistamentoNaoEncontradoException;
import br.insper.arqui.exception.ValidacaoEnlistamentoException;
import br.insper.arqui.repository.EnlistamentoRepository;
import br.insper.arqui.validator.ValidadorEnlistamento;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnlistamentoService {

	private final EnlistamentoRepository enlistamentoRepository;
	private final ValidadorEnlistamento validadorEnlistamento;
	private final JogosClient jogosClient;

	public EnlistamentoService(
			EnlistamentoRepository enlistamentoRepository,
			ValidadorEnlistamento validadorEnlistamento,
			JogosClient jogosClient
	) {
		this.enlistamentoRepository = enlistamentoRepository;
		this.validadorEnlistamento = validadorEnlistamento;
		this.jogosClient = jogosClient;
	}

	public Enlistamento criar(EnlistamentoDto dto) {
		validadorEnlistamento.validar(dto);
		validarJogo(dto.getJogoId());
		validarDuplicidade(dto, null);
		return enlistamentoRepository.save(Enlistamento.fromDto(dto));
	}

	public List<Enlistamento> listarTodos() {
		return enlistamentoRepository.findAll();
	}

	public List<Enlistamento> listarPorPasta(Pasta pasta) {
		if (pasta == null) {
			throw new ValidacaoEnlistamentoException("Pasta é obrigatória");
		}
		return enlistamentoRepository.findAllByPasta(pasta);
	}

	public Enlistamento obterPorId(Long id) {
		return enlistamentoRepository.findById(id)
				.orElseThrow(() -> new EnlistamentoNaoEncontradoException(id));
	}

	public Enlistamento atualizar(Long id, EnlistamentoDto dto) {
		validadorEnlistamento.validar(dto);
		validarJogo(dto.getJogoId());

		Enlistamento enlistamento = obterPorId(id);
		validarDuplicidade(dto, enlistamento);

		enlistamento.setJogoId(dto.getJogoId());
		enlistamento.setPasta(dto.getPasta());

		return enlistamentoRepository.save(enlistamento);
	}

	public void deletar(Long id) {
		Enlistamento enlistamento = obterPorId(id);
		enlistamentoRepository.delete(enlistamento);
	}

	private void validarJogo(Long jogoId) {
		if (!jogosClient.jogoExiste(jogoId)) {
			throw new ValidacaoEnlistamentoException("Jogo não encontrado");
		}
	}

	private void validarDuplicidade(EnlistamentoDto dto, Enlistamento atual) {
		boolean dadosNaoMudaram = atual != null
				&& atual.getJogoId().equals(dto.getJogoId())
				&& atual.getPasta() == dto.getPasta();

		if (!dadosNaoMudaram
				&& enlistamentoRepository.existsByJogoIdAndPasta(
						dto.getJogoId(),
						dto.getPasta()
				)) {
			throw new ValidacaoEnlistamentoException("O jogo já está nessa pasta");
		}
	}
}