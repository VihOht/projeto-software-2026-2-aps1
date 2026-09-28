package br.insper.pagamento.service;

import br.insper.pagamento.dto.EnlistamentoDto;
import br.insper.pagamento.entity.Enlistamento;
import br.insper.pagamento.entity.Pasta;
import br.insper.pagamento.exception.EnlistamentoNaoEncontradoException;
import br.insper.pagamento.exception.ValidacaoEnlistamentoException;
import br.insper.pagamento.repository.EnlistamentoRepository;
import br.insper.pagamento.validator.ValidadorEnlistamento;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnlistamentoService {

	private final EnlistamentoRepository enlistamentoRepository;
	private final ValidadorEnlistamento validadorEnlistamento;

	public EnlistamentoService(
			EnlistamentoRepository enlistamentoRepository,
			ValidadorEnlistamento validadorEnlistamento
	) {
		this.enlistamentoRepository = enlistamentoRepository;
		this.validadorEnlistamento = validadorEnlistamento;
	}

	public Enlistamento criar(EnlistamentoDto dto) {
		validadorEnlistamento.validar(dto);
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

	private void validarDuplicidade(EnlistamentoDto dto, Enlistamento atual) {
		boolean dadosNaoMudaram = atual != null
				&& atual.getJogoId().equals(dto.getJogoId())
				&& atual.getPasta() == dto.getPasta();

		if (!dadosNaoMudaram && enlistamentoRepository.existsByJogoIdAndPasta(dto.getJogoId(), dto.getPasta())) {
			throw new ValidacaoEnlistamentoException("O jogo já está nessa pasta");
		}
	}
}
