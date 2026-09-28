package br.insper.arqui.validator;

import br.insper.arqui.dto.JogosDto;
import br.insper.arqui.exception.ValidacaoJogosException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorJogos {

	public void validar(JogosDto dto) {
		if (dto == null) {
			throw new ValidacaoJogosException("Os dados do jogo são obrigatórios");
		}
		if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
			throw new ValidacaoJogosException("Título é obrigatório");
		}
		if (dto.getDataLancamento() == null) {
			throw new ValidacaoJogosException("Data de lançamento é obrigatória");
		}
		if (dto.getDuracaoHoras() == null || dto.getDuracaoHoras() <= 0) {
			throw new ValidacaoJogosException("Duração deve ser maior que zero");
		}
		if (dto.getTipoJogo() == null || dto.getTipoJogo().isBlank()) {
			throw new ValidacaoJogosException("Tipo do jogo é obrigatório");
		}
		if (dto.getPasta() == null) {
			throw new ValidacaoJogosException("Pasta é obrigatória");
		}
		if (dto.getTrilhaSonora() == null || dto.getTrilhaSonora().isBlank()) {
			throw new ValidacaoJogosException("Trilha sonora é obrigatória");
		}
		if (dto.getDesenvolvedora() == null || dto.getDesenvolvedora().isBlank()) {
			throw new ValidacaoJogosException("Desenvolvedora é obrigatória");
		}
		if (dto.getPlataforma() == null || dto.getPlataforma().isBlank()) {
			throw new ValidacaoJogosException("Plataforma é obrigatória");
		}
	}
}
