package br.insper.arqui.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class JogosNaoEncontradoException extends RuntimeException {
	public JogosNaoEncontradoException(Long id) {
		super("Jogo com ID " + id + " não encontrado");
	}
}
