package br.insper.pagamento.validator;

import br.insper.pagamento.dto.MusicasDto;
import br.insper.pagamento.exception.ValidacaoMusicasException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMusicas {

	public void validar(MusicasDto dto) {
		if (dto == null) {
			throw new ValidacaoMusicasException("Os dados da música são obrigatórios");
		}
		if (dto.getJogoId() == null || dto.getJogoId() <= 0) {
			throw new ValidacaoMusicasException("ID do jogo deve ser maior que zero");
		}
		if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
			throw new ValidacaoMusicasException("Título é obrigatório");
		}
		if (dto.getArtista() == null || dto.getArtista().isBlank()) {
			throw new ValidacaoMusicasException("Artista é obrigatório");
		}
		if (dto.getDataLancamento() == null) {
			throw new ValidacaoMusicasException("Data de lançamento é obrigatória");
		}
		if (dto.getDuracaoSegundos() == null || dto.getDuracaoSegundos() <= 0) {
			throw new ValidacaoMusicasException("Duração deve ser maior que zero");
		}
	}
}
