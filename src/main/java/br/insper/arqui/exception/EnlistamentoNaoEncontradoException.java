package br.insper.arqui.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EnlistamentoNaoEncontradoException extends RuntimeException {
	public EnlistamentoNaoEncontradoException(Long id) {
		super("Enlistamento com ID " + id + " não encontrado");
	}
}
