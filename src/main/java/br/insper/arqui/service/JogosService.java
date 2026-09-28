package br.insper.arqui.service;

import br.insper.arqui.dto.JogosDto;
import br.insper.arqui.entity.Jogos;
import br.insper.arqui.entity.Pasta;
import br.insper.arqui.exception.JogosNaoEncontradoException;
import br.insper.arqui.exception.ValidacaoJogosException;
import br.insper.arqui.repository.JogosRepository;
import br.insper.arqui.validator.ValidadorJogos;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JogosService {

	private final JogosRepository jogosRepository;
	private final ValidadorJogos validadorJogos;

	public JogosService(JogosRepository jogosRepository, ValidadorJogos validadorJogos) {
		this.jogosRepository = jogosRepository;
		this.validadorJogos = validadorJogos;
	}

	public Jogos criar(JogosDto dto) {
		validadorJogos.validar(dto);
		validarDuplicidade(dto, null);
		return jogosRepository.save(Jogos.fromDto(dto));
	}

	public List<Jogos> listarTodos() {
		return jogosRepository.findAll();
	}

	public List<Jogos> listarPorPasta(Pasta pasta) {
		if (pasta == null) {
			throw new ValidacaoJogosException("Pasta é obrigatória");
		}
		return jogosRepository.findAllByPasta(pasta);
	}

	public Jogos obterPorId(Long id) {
		return jogosRepository.findById(id)
				.orElseThrow(() -> new JogosNaoEncontradoException(id));
	}

	public Jogos atualizar(Long id, JogosDto dto) {
		validadorJogos.validar(dto);
		Jogos jogo = obterPorId(id);
		validarDuplicidade(dto, jogo);
		jogo.setTitulo(dto.getTitulo());
		jogo.setDataLancamento(dto.getDataLancamento());
		jogo.setDuracaoHoras(dto.getDuracaoHoras());
		jogo.setTipoJogo(dto.getTipoJogo());
		jogo.setPasta(dto.getPasta());
		jogo.setTrilhaSonora(dto.getTrilhaSonora());
		jogo.setDesenvolvedora(dto.getDesenvolvedora());
		jogo.setPlataforma(dto.getPlataforma());
		return jogosRepository.save(jogo);
	}

	public void deletar(Long id) {
		Jogos jogo = obterPorId(id);
		jogosRepository.delete(jogo);
	}

	private void validarDuplicidade(JogosDto dto, Jogos atual) {
		boolean dadosNaoMudaram = atual != null
				&& atual.getTitulo().equals(dto.getTitulo())
				&& atual.getPlataforma().equals(dto.getPlataforma());

		if (!dadosNaoMudaram && jogosRepository.existsByTituloAndPlataforma(
				dto.getTitulo(), dto.getPlataforma())) {
			throw new ValidacaoJogosException("O jogo já está cadastrado para essa plataforma");
		}
	}
}
