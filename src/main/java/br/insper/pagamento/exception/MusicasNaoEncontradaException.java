package br.insper.pagamento.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MusicasNaoEncontradaException extends RuntimeException {
	public MusicasNaoEncontradaException(Long id) {
		super("Música com ID " + id + " não encontrada");
	}
}
